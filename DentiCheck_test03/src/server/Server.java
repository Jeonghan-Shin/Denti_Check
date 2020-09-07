package server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Server extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JLabel port_label;
	private JTextField port_tf;
	private JButton start_btn;
	private JButton stop_btn;

	private ServerSocket server_socket;
	private Socket socket;
	private int port;

	private Vector user_vc = new Vector();
	private Vector room_vc = new Vector();

	private StringTokenizer st;

	Server() {
		init();
		start(); // 리스너 설정 메소드
	}

	private void start() {
		start_btn.addActionListener(this);
		stop_btn.addActionListener(this);
	}

	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 12, 655, 520);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);

		port_label = new JLabel("포트 번호 : ");
		port_label.setBounds(195, 562, 95, 18);
		contentPane.add(port_label);

		port_tf = new JTextField();
		port_tf.setBounds(275, 560, 236, 24);
		contentPane.add(port_tf);
		port_tf.setColumns(10);
		port_tf.setText("7777");

		start_btn = new JButton("서버 실행");
		start_btn.setBounds(210, 600, 147, 27);
		contentPane.add(start_btn);

		stop_btn = new JButton("서버 중단");
		stop_btn.setBounds(350, 600, 147, 27);
		contentPane.add(stop_btn);
		stop_btn.setEnabled(false);

		setVisible(true);
	}

	private void Server_start() {
		try {
			server_socket = new ServerSocket(port);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "이미 사용중인 포트", "알림", JOptionPane.ERROR_MESSAGE);
		}

		if (server_socket != null) {
			Connection();
		}

	}

	private void Connection() {
		// 1가지의 스레드에서는 1가지의 일만 처리할 수 있다.
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() { // 스레드에서 처리할 일을 기재한다.

				while (true) {

					try {
						textArea.append("사용자 접속 대기중\n");
						scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // 자동
																													// 스크롤

						socket = server_socket.accept(); // 사용자 접속 무한 대기

						UserInfo user = new UserInfo(socket);

						user.start(); // 개별적으로 객체의 스레드를 실행(각각의 user의 송수신 담당)

					} catch (IOException e) {
						break;
					}

				}

			}
		});

		th.start();
	}

	public static void main(String[] args) {
		new Server();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start_btn) {

			textArea.append("--- 서버 실행 ---\n");
			port = Integer.parseInt(port_tf.getText().trim());
			Server_start();
			start_btn.setEnabled(false);
			stop_btn.setEnabled(true);
			port_tf.setEditable(false);

		} else if (e.getSource() == stop_btn) {

			textArea.append("--- 서버 종료 ---\n");
			start_btn.setEnabled(true);
			stop_btn.setEnabled(false);
			port_tf.setEditable(true);

			try {
				server_socket.close();
				socket.close();
				user_vc.removeAllElements();
				room_vc.removeAllElements();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // 자동 스크롤

	}

	class UserInfo extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private Socket user_socket;
		private String id = "";

		private String fileServerIP = "";
		private String filePath = "";
		private String fileName = "";

		private boolean RoomCh = true;

		public UserInfo(Socket soc) {
			this.user_socket = soc;
			UserNetwork();
		}

		private void UserNetwork() { // 네트워크 자원 설정
			try {
				is = user_socket.getInputStream();
				dis = new DataInputStream(is);

				os = user_socket.getOutputStream();
				dos = new DataOutputStream(os);

				id = dis.readUTF(); // 사용자의 닉네임을 받는다.
				textArea.append("** " + id + " : 사용자 접속 **\n");

				Broadcast("NewUser|" + id);// 기존 사용자들에게 새로운 사용자 알림

				// 자신에게 기존 사용자를 받아 옴
				for (int i = 0; i < user_vc.size(); i++) {
					UserInfo u = (UserInfo) user_vc.elementAt(i);
					send_Message("OldUser|" + u.id);
				}

				// 자신에게 기존 방 목록을 받아 옴
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					send_Message("OldRoom|" + r.Room_name);
				}

				send_Message("room_list_update| ");

				user_vc.add(this); // 사용자에게 알린 후 vector에 자신을 추가

				Broadcast("user_list_update| "); // 사용자 리스트 갱신 protocol 전송

				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // 자동 스크롤

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Stream 설정 에러", "알림", JOptionPane.ERROR_MESSAGE);
			}

		}

		public void run() { // thread에서 처리할 내용
			while (true) {
				try {
					String msg = dis.readUTF();
					textArea.append(id + " 로부터 들어온 메세지 : " + msg + "\n");
					inMessage(msg);

				} catch (IOException e) {
					textArea.append("** " + id + " 접속 종료 **\n");

					// 원래 속해있던 방을 찾는다
					for (int i = 0; i < room_vc.size(); i++) {
						RoomInfo r = (RoomInfo) room_vc.elementAt(i);

						for (int j = 0; j < r.Room_user_vc.size(); j++) { // 해당 방을 찾았을 때
							if (r.Room_user_vc.elementAt(j).equals(this)) {
								// 사용자 삭제
								r.remove_User(this);

								// 사용자가 나갔음을 알린다
								r.Broadcast_Room("Notifying|" + id + "님이 퇴장하였습니다");

								checkRoomRemove(r); // 방에 아무도 없는 경우 방 삭제

							}
						}

					}

					try {
						dos.close();
						dis.close();
						user_socket.close();
					} catch (IOException e1) {
					}
					user_vc.remove(this);
					Broadcast("User_out|" + id);
					Broadcast("user_list_update| ");
					break;
				}

				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // 자동 스크롤

			}

		}

		private void inMessage(String str) { // 클라이언트로부터 들어오늘 메세지 처리 (프로토콜)
			st = new StringTokenizer(str, "|");

			String protocol = st.nextToken();
			String message = st.nextToken();

//			System.out.println("프로토콜 : " + protocol);
//			System.out.println("내용 : " + message);

			if (protocol.equals("Note")) { // 새로운 접속자

				String note = st.nextToken();

//				System.out.println("받는 사람: " + message); // receiver는 message
//				System.out.println("보낼 내용: " + note);

				// 벡터에서 해당 사용자룰 찾아서 메세지 전송
				for (int i = 0; i < user_vc.size(); i++) {
					UserInfo u = (UserInfo) user_vc.elementAt(i);

					if (u.id.equals(message)) {
						u.send_Message("Note|" + id + "|" + note);
					}
				}

			} else if (protocol.equals("CreateRoom")) { // 방 만들기 프로토콜
				// 현재 같은 방이 존재하는지 확인
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);

					if (r.Room_name.equals(message)) { // 만들고자하는 방이 이미 존재 할 때
						send_Message("CreateRoomFail|ok"); // 방 만들기 실패 프로토콜 전송
						RoomCh = false;
						break;
					}
				}

				if (RoomCh) { // 방을 만들 수 있을 때
					RoomInfo new_room = new RoomInfo(message, this);
					room_vc.add(new_room); // 전체 방 벡터에 방을 추가

					send_Message("CreateRoom|" + message);

					Broadcast("New_Room|" + message);
				}

				RoomCh = true; // 다시 원래 방을 만들 수 있는 상태로 set

			} else if (protocol.equals("Chatting")) { // 방에서 채팅하기 프로토콜 (입력 문자를 받음)
				String msg = st.nextToken();

				// 방을 찾는다
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);

					if (r.Room_name.equals(message)) { // 해당 방을 찾았을 때
						// 방에 있는 사용자들에게 메세지를 보냄
						r.Broadcast_Room("Chatting|" + id + "|" + msg);
					}
				}

			} else if (protocol.equals("JoinRoom")) { // 방에 참가하기
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					if (r.Room_name.equals(message)) {
						// 새로운 사용자를 알린다
						r.Broadcast_Room("Notifying|" + id + " 님이 입장하였습니다");

						// 사용자 추가
						r.Add_User(this);
						send_Message("JoinRoom|" + r.Room_name);
					}
				}
			} else if (protocol.equals("Room_out")) { // 방 나가기
				// 방을 찾는다
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);

					if (r.Room_name.equals(message)) { // 해당 방을 찾았을 때
						// 사용자가 나갔음을 알린다
						r.Broadcast_Room("Notifying|" + id + " 님이 퇴장하였습니다");

						// 사용자 삭제
						send_Message("Room_out| ");
						r.remove_User(this);

						checkRoomRemove(r); // 방에 아무도 없는 경우 방 삭제

					}
				}

			} else if (protocol.equals("FileSendTry")) { // 파일 전송 시도
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					if (r.Room_name.equals(message)) {
						filePath = st.nextToken();
						File sendFile = new File(filePath);
						fileName = sendFile.getName();
						String availExtList = "jpeg,jpg,png,gif,bmp";

//						if (sendFile.isFile()) {
							String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);
							if (availExtList.contains(fileExt)) {
//								Socket s = this.user_socket;
//								fileServerIP = s.getInetAddress().getHostAddress();
								fileServerIP = this.user_socket.getInetAddress().getHostAddress();
								r.BroadcastOthers_Room("RequestSendFile|" + id + "|님께서 파일 전송을 시도합니다.", this);
							} else {
								send_Message("Notifying|전송 가능한 파일형식이 아닙니다. [" + availExtList + "] 파일만 가능. ");
							}

//						} else {
//							send_Message("Notifying|존재하지 않는 파일입니다.");
//						}
					}
				}

			} else if (protocol.equals("FileAccept")) { // 파일 전송 수락
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					if (r.Room_name.equals(message)) {
						r.BroadcastOthers_Room("SendFile| ", this);
					}
				}
				/////////////////////////////////////////////////
				/////////////////////////////////////////////////

			} else if (protocol.equals("FileRefuse")) { // 파일 전송 거절
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					if (r.Room_name.equals(message)) {
						r.BroadcastOthers_Room("KeepFile|" + id + "님이 파일 전송을 거절하였습니다", this);
					}
				}
				/////////////////////////////////////////////////
				/////////////////////////////////////////////////

			} else if (protocol.equals("FileReceiver")) { // 파일을 받는다
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					if (r.Room_name.equals(message)) {

						r.BroadcastOthers_Room("ReceiveFile|" + fileServerIP + "|" + fileName, this);
					}
				}
				fileServerIP = "";
				filePath = "";
				fileName = "";

			} else if (protocol.equals("FileComplete")) {
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					if (r.Room_name.equals(message)) {

						Broadcast("FileEnd| ");
					}
				}
			}

		}

		private void Broadcast(String str) { // 전체 사용자에게 메세지 전송
			for (int i = 0; i < user_vc.size(); i++) {
				UserInfo u = (UserInfo) user_vc.elementAt(i);
				u.send_Message(str);
			}
		}

		private void send_Message(String str) { // 문자열을 받아서 전송
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
			}
		}

		private void checkRoomRemove(RoomInfo r) {
			if (r.isEmpty()) {
				textArea.append("** " + r.Room_name + " 채팅방 삭제 **\n");
				room_vc.remove(r);
				Broadcast("Room_remove|" + r.Room_name);
				Broadcast("room_list_update| ");

			}
		}

	}

	class RoomInfo {

		private String Room_name;
		private Vector Room_user_vc = new Vector();

		RoomInfo(String room_name, UserInfo u) {
			this.Room_name = room_name;
			this.Room_user_vc.add(u);
		}

		public void Broadcast_Room(String str) { // 현재 방의 모든 사람에게 메세지 전송
			for (int i = 0; i < Room_user_vc.size(); i++) {
				UserInfo u = (UserInfo) Room_user_vc.elementAt(i);

				u.send_Message(str);
			}

		}

		public void BroadcastOthers_Room(String str, UserInfo user) {
			for (int i = 0; i < Room_user_vc.size(); i++) {
				UserInfo u = (UserInfo) Room_user_vc.elementAt(i);

				if (u.id != user.id)
					u.send_Message(str);
			}
		}

		public void Add_User(UserInfo u) {
			this.Room_user_vc.add(u);
		}

		public void remove_User(UserInfo u) {
			this.Room_user_vc.remove(u);
		}

		public boolean isEmpty() {
			if (Room_user_vc.size() == 0)
				return true;
			else
				return false;
		}

	}

}
