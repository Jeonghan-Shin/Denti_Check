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
		start(); // ������ ���� �޼ҵ�
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

		port_label = new JLabel("��Ʈ ��ȣ : ");
		port_label.setBounds(195, 562, 95, 18);
		contentPane.add(port_label);

		port_tf = new JTextField();
		port_tf.setBounds(275, 560, 236, 24);
		contentPane.add(port_tf);
		port_tf.setColumns(10);
		port_tf.setText("7777");

		start_btn = new JButton("���� ����");
		start_btn.setBounds(210, 600, 147, 27);
		contentPane.add(start_btn);

		stop_btn = new JButton("���� �ߴ�");
		stop_btn.setBounds(350, 600, 147, 27);
		contentPane.add(stop_btn);
		stop_btn.setEnabled(false);

		setVisible(true);
	}

	private void Server_start() {
		try {
			server_socket = new ServerSocket(port);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "�̹� ������� ��Ʈ", "�˸�", JOptionPane.ERROR_MESSAGE);
		}

		if (server_socket != null) {
			Connection();
		}

	}

	private void Connection() {
		// 1������ �����忡���� 1������ �ϸ� ó���� �� �ִ�.
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() { // �����忡�� ó���� ���� �����Ѵ�.

				while (true) {

					try {
						textArea.append("����� ���� �����\n");
						scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // �ڵ�
																													// ��ũ��

						socket = server_socket.accept(); // ����� ���� ���� ���

						UserInfo user = new UserInfo(socket);

						user.start(); // ���������� ��ü�� �����带 ����(������ user�� �ۼ��� ���)

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

			textArea.append("--- ���� ���� ---\n");
			port = Integer.parseInt(port_tf.getText().trim());
			Server_start();
			start_btn.setEnabled(false);
			stop_btn.setEnabled(true);
			port_tf.setEditable(false);

		} else if (e.getSource() == stop_btn) {

			textArea.append("--- ���� ���� ---\n");
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

		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // �ڵ� ��ũ��

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

		private void UserNetwork() { // ��Ʈ��ũ �ڿ� ����
			try {
				is = user_socket.getInputStream();
				dis = new DataInputStream(is);

				os = user_socket.getOutputStream();
				dos = new DataOutputStream(os);

				id = dis.readUTF(); // ������� �г����� �޴´�.
				textArea.append("** " + id + " : ����� ���� **\n");

				Broadcast("NewUser|" + id);// ���� ����ڵ鿡�� ���ο� ����� �˸�

				// �ڽſ��� ���� ����ڸ� �޾� ��
				for (int i = 0; i < user_vc.size(); i++) {
					UserInfo u = (UserInfo) user_vc.elementAt(i);
					send_Message("OldUser|" + u.id);
				}

				// �ڽſ��� ���� �� ����� �޾� ��
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					send_Message("OldRoom|" + r.Room_name);
				}

				send_Message("room_list_update| ");

				user_vc.add(this); // ����ڿ��� �˸� �� vector�� �ڽ��� �߰�

				Broadcast("user_list_update| "); // ����� ����Ʈ ���� protocol ����

				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // �ڵ� ��ũ��

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Stream ���� ����", "�˸�", JOptionPane.ERROR_MESSAGE);
			}

		}

		public void run() { // thread���� ó���� ����
			while (true) {
				try {
					String msg = dis.readUTF();
					textArea.append(id + " �κ��� ���� �޼��� : " + msg + "\n");
					inMessage(msg);

				} catch (IOException e) {
					textArea.append("** " + id + " ���� ���� **\n");

					// ���� �����ִ� ���� ã�´�
					for (int i = 0; i < room_vc.size(); i++) {
						RoomInfo r = (RoomInfo) room_vc.elementAt(i);

						for (int j = 0; j < r.Room_user_vc.size(); j++) { // �ش� ���� ã���� ��
							if (r.Room_user_vc.elementAt(j).equals(this)) {
								// ����� ����
								r.remove_User(this);

								// ����ڰ� �������� �˸���
								r.Broadcast_Room("Notifying|" + id + "���� �����Ͽ����ϴ�");

								checkRoomRemove(r); // �濡 �ƹ��� ���� ��� �� ����

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

				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // �ڵ� ��ũ��

			}

		}

		private void inMessage(String str) { // Ŭ���̾�Ʈ�κ��� ������ �޼��� ó�� (��������)
			st = new StringTokenizer(str, "|");

			String protocol = st.nextToken();
			String message = st.nextToken();

//			System.out.println("�������� : " + protocol);
//			System.out.println("���� : " + message);

			if (protocol.equals("Note")) { // ���ο� ������

				String note = st.nextToken();

//				System.out.println("�޴� ���: " + message); // receiver�� message
//				System.out.println("���� ����: " + note);

				// ���Ϳ��� �ش� ����ڷ� ã�Ƽ� �޼��� ����
				for (int i = 0; i < user_vc.size(); i++) {
					UserInfo u = (UserInfo) user_vc.elementAt(i);

					if (u.id.equals(message)) {
						u.send_Message("Note|" + id + "|" + note);
					}
				}

			} else if (protocol.equals("CreateRoom")) { // �� ����� ��������
				// ���� ���� ���� �����ϴ��� Ȯ��
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);

					if (r.Room_name.equals(message)) { // ��������ϴ� ���� �̹� ���� �� ��
						send_Message("CreateRoomFail|ok"); // �� ����� ���� �������� ����
						RoomCh = false;
						break;
					}
				}

				if (RoomCh) { // ���� ���� �� ���� ��
					RoomInfo new_room = new RoomInfo(message, this);
					room_vc.add(new_room); // ��ü �� ���Ϳ� ���� �߰�

					send_Message("CreateRoom|" + message);

					Broadcast("New_Room|" + message);
				}

				RoomCh = true; // �ٽ� ���� ���� ���� �� �ִ� ���·� set

			} else if (protocol.equals("Chatting")) { // �濡�� ä���ϱ� �������� (�Է� ���ڸ� ����)
				String msg = st.nextToken();

				// ���� ã�´�
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);

					if (r.Room_name.equals(message)) { // �ش� ���� ã���� ��
						// �濡 �ִ� ����ڵ鿡�� �޼����� ����
						r.Broadcast_Room("Chatting|" + id + "|" + msg);
					}
				}

			} else if (protocol.equals("JoinRoom")) { // �濡 �����ϱ�
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					if (r.Room_name.equals(message)) {
						// ���ο� ����ڸ� �˸���
						r.Broadcast_Room("Notifying|" + id + " ���� �����Ͽ����ϴ�");

						// ����� �߰�
						r.Add_User(this);
						send_Message("JoinRoom|" + r.Room_name);
					}
				}
			} else if (protocol.equals("Room_out")) { // �� ������
				// ���� ã�´�
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);

					if (r.Room_name.equals(message)) { // �ش� ���� ã���� ��
						// ����ڰ� �������� �˸���
						r.Broadcast_Room("Notifying|" + id + " ���� �����Ͽ����ϴ�");

						// ����� ����
						send_Message("Room_out| ");
						r.remove_User(this);

						checkRoomRemove(r); // �濡 �ƹ��� ���� ��� �� ����

					}
				}

			} else if (protocol.equals("FileSendTry")) { // ���� ���� �õ�
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
								r.BroadcastOthers_Room("RequestSendFile|" + id + "|�Բ��� ���� ������ �õ��մϴ�.", this);
							} else {
								send_Message("Notifying|���� ������ ���������� �ƴմϴ�. [" + availExtList + "] ���ϸ� ����. ");
							}

//						} else {
//							send_Message("Notifying|�������� �ʴ� �����Դϴ�.");
//						}
					}
				}

			} else if (protocol.equals("FileAccept")) { // ���� ���� ����
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					if (r.Room_name.equals(message)) {
						r.BroadcastOthers_Room("SendFile| ", this);
					}
				}
				/////////////////////////////////////////////////
				/////////////////////////////////////////////////

			} else if (protocol.equals("FileRefuse")) { // ���� ���� ����
				for (int i = 0; i < room_vc.size(); i++) {
					RoomInfo r = (RoomInfo) room_vc.elementAt(i);
					if (r.Room_name.equals(message)) {
						r.BroadcastOthers_Room("KeepFile|" + id + "���� ���� ������ �����Ͽ����ϴ�", this);
					}
				}
				/////////////////////////////////////////////////
				/////////////////////////////////////////////////

			} else if (protocol.equals("FileReceiver")) { // ������ �޴´�
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

		private void Broadcast(String str) { // ��ü ����ڿ��� �޼��� ����
			for (int i = 0; i < user_vc.size(); i++) {
				UserInfo u = (UserInfo) user_vc.elementAt(i);
				u.send_Message(str);
			}
		}

		private void send_Message(String str) { // ���ڿ��� �޾Ƽ� ����
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
			}
		}

		private void checkRoomRemove(RoomInfo r) {
			if (r.isEmpty()) {
				textArea.append("** " + r.Room_name + " ä�ù� ���� **\n");
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

		public void Broadcast_Room(String str) { // ���� ���� ��� ������� �޼��� ����
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
