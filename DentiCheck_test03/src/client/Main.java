package client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import webCam.CamCap;

public class Main extends JFrame implements ActionListener, KeyListener, MouseListener {

	// 로그인 화면 // Denti_UI
	private JFrame Login_UI = new JFrame();
	private JPanel login_panel;
	private JTextField id_tf;
	private JPasswordField pw_tf;
	private JButton exit_btn;
	private JButton login_btn;
	private JLabel join_lb;
	// 회원가입 구분 // Denti_UI1
	private JFrame joinMemCheck_ui = new JFrame();
	private JPanel MemCheckContentPane;
	private JButton select_clients_btn;
	private JButton select_clinics_btn;
	// 회원가입 (개인) // Denti_UI1_0
	private JFrame joinMemClient_ui = new JFrame();
	private JPanel JoinClientContentPane;
	private JTextField id_clients_tf;
	private JTextField name_clients_tf;
	private JTextField num_clients_tf;
	private JTextField birth_clients_tf;
	private JPasswordField pw_clients_pf;
	private JPasswordField repeatpw_clients_pf;
	private JButton join_clients_btn;
	// 회원가입 (치과) // Denti_UI1_1
	private JFrame joinMemClinic_ui = new JFrame();
	private JPanel JoinClinicContentPane;
	private JTextField id_clinics_tf;
	private JTextField name_clinics_tf;
	private JTextField num_clinics_tf;
	private JTextField address_clinics_tf;
	private JPasswordField pw_clinics_pf;
	private JPasswordField repeatpw_clinics_pf;
	private JButton join_clinics_btn;
	// 메인 패널
	private JPanel mainContentPane = new JPanel();
	private CardLayout card = new CardLayout();
	// 서비스 선택 화면 // Denti_UI2_0
	private JPanel serviceContentPane;
	private JButton menu1_btn;
	private JButton menu2_btn;
	private JButton menu3_btn;
	private JButton logout_btn;
	// 원격진료 (치과 검색) // Denti_UI2_1
	private JPanel findContentPane;
	private JButton back_1_btn;
	private JButton ok1_btn;
	private JButton ok2_btn;
	private JTextField select_tf;
	private JComboBox do_cb;
	private JComboBox gu_cb;
	// 자가진단 // Denti_UI2_2
	private JPanel selfContentPane;
	private JButton select1_btn;
	private JButton select2_btn;
	private JButton select3_btn;
	private JButton select4_btn;
	private JButton select5_btn;
	private JButton back_btn;
	private JButton camera_btn;
	private JLabel label1_lb;
	private JLabel label2_lb;
	private JLabel label3_lb;
	private JLabel label4_lb;
	private JLabel label5_lb;
	private JLabel label0_lb;
	// 건강정보 // Denti_UI2_3
	private JPanel infoContentPane;
	private JButton select3_1_btn;
	private JButton select3_2_btn;
	private JButton select3_3_btn;
	private JButton select3_4_btn;
	private JButton select3_5_btn;
	private JButton select3_6_btn;
	private JButton back_2_btn;
	private JLabel label3_1_lb;
	private JLabel label3_2_lb;
	private JLabel label3_3_lb;
	private JLabel label3_4_lb;
	private JLabel label3_5_lb;
	private JLabel label3_6_lb;
	private JLabel label3_0_lb;
	// 치과 리스트 창 // ChatRoomList
	private JPanel listContentPane;
	private JScrollPane listScrollPane;
	private JList dentistList;
	private JButton care_btn;
	private JButton back_3_btn;
	// 채팅 창 // ChattingFrame
	private JPanel ChattingContentPane;
	private JTextField write_tf;
	private JScrollPane chatScrollPane;
	private JButton sendMsg_btn;
	private JButton sendPhoto_btn;
	private JButton camera_2_btn;
	private JButton quitChat_btn;
	private JTextArea msg_ta;
	// 채팅 창 (치과용) // ChattingFrame_for_Dentist
	private JPanel ChattingContentPane_D;
	private JTextField write_tf_D;
	private JScrollPane chatScrollPane_D;
	private JButton sendMsg_btn_D;
	private JButton sendPhoto_btn_D;
	private JButton startChat_btn_D;
	private JButton quitChat_btn_D;
	private JTextArea msg_ta_D;

	// 네트워크를 위한 자원 변수
	private Socket socket;
//	private String ip = "223.194.42.204"; // 서버의 IP 주소
	private String ip = "127.0.0.1"; // 서버의 IP 주소
	private int port = 7777; // port 번호
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	// 기타 변수
	private String id = "";
	private String password;
	private int checkCilent = 1; // 일반 고객: 1, 치과: 2

	private String My_Room = "";

	private StringTokenizer st;

	private Vector users = new Vector();
	private Vector rooms = new Vector();

	String filePath = "";

	public Main() {
		loginFrame_init(); // 로그인 화면 // Denti_UI
		joinMemCheckFrame(); // 회원가입 구분 // Denti_UI1
		joinClientFrame(); // 회원가입 (개인) // Denti_UI1_0
		joinClinicFrame(); // 회원가입 (치과) // Denti_UI1_1
		MainFrame_init(); // 서비스 선택 화면 // Denti_UI2_0
		selfCheckPanel(); // 자가진단 // Denti_UI2_2
		informationPanel(); // 건강정보 // Denti_UI2_3
		findDentistPanel(); // 원격진료 (치과 검색) // Denti_UI2_1
		roomListPanel(); // 치과 리스트 창 // ChatRoomList
		chattingPanel(); // 채팅 창 // ChattingFrame
		chattingForDenPanel(); // 채팅 창 (치과용) // ChattingFrame_for_Dentist

		start();
	}

	// 버튼 리스너 활성화
	private void start() {
		login_btn.addActionListener(this);
		exit_btn.addActionListener(this);
		join_lb.addMouseListener(this);
		menu1_btn.addActionListener(this);
		menu2_btn.addActionListener(this);
		menu3_btn.addActionListener(this);
		logout_btn.addActionListener(this);
		select1_btn.addActionListener(this);
		select2_btn.addActionListener(this);
		select3_btn.addActionListener(this);
		select4_btn.addActionListener(this);
		select5_btn.addActionListener(this);
		back_btn.addActionListener(this);
		camera_btn.addActionListener(this);
		select3_1_btn.addActionListener(this);
		select3_2_btn.addActionListener(this);
		select3_3_btn.addActionListener(this);
		select3_4_btn.addActionListener(this);
		select3_5_btn.addActionListener(this);
		select3_6_btn.addActionListener(this);
		back_2_btn.addActionListener(this);
		select_clients_btn.addActionListener(this);
		select_clinics_btn.addActionListener(this);
		join_clients_btn.addActionListener(this);
		join_clinics_btn.addActionListener(this);
		back_1_btn.addActionListener(this);
		ok1_btn.addActionListener(this);
		ok2_btn.addActionListener(this);
		care_btn.addActionListener(this);
		back_3_btn.addActionListener(this);
		sendMsg_btn.addActionListener(this);
		sendPhoto_btn.addActionListener(this);
		quitChat_btn.addActionListener(this);
		sendMsg_btn_D.addActionListener(this);
		sendPhoto_btn_D.addActionListener(this);
		startChat_btn_D.addActionListener(this);
		quitChat_btn_D.addActionListener(this);

		write_tf.addKeyListener(this);
		write_tf_D.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) { // Enter 키가 눌려졌을 경우, 전송 버튼을 누르는 것과 동일한 동작
					String m = write_tf_D.getText().trim();

					if (m.length() != 0) {
						send_message("Chatting|" + My_Room + "| " + m);
						write_tf_D.setText("");
						write_tf_D.requestFocus();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void loginFrame_init() {
		Login_UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Login_UI.setBounds(100, 100, 400, 600);
		Login_UI.setLocationRelativeTo(null);
		String IconPath = ".\\images\\icons\\simbol.PNG";
		ImageIcon SimbolIcon = new ImageIcon(IconPath);
		Login_UI.setIconImage(SimbolIcon.getImage());
		Login_UI.setUndecorated(true);
		login_panel = new JPanel();
		login_panel.setBackground(SystemColor.activeCaption);

		Login_UI.getContentPane().setLayout(null);
		login_panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		Login_UI.setContentPane(login_panel);
		login_panel.setLayout(null);

		JLabel LabelID = new JLabel("ID");
		LabelID.setForeground(SystemColor.textHighlightText);
		LabelID.setFont(new Font("Arial", Font.BOLD, 16));
		LabelID.setBounds(104, 250, 32, 29);
		Login_UI.getContentPane().add(LabelID);

		JLabel LabelPW = new JLabel("PW");
		LabelPW.setForeground(SystemColor.textHighlightText);
		LabelPW.setFont(new Font("Arial", Font.BOLD, 16));
		LabelPW.setBounds(97, 290, 32, 29);
		Login_UI.getContentPane().add(LabelPW);

		login_btn = new JButton("Log In");
		login_btn.setForeground(new Color(0, 0, 0));
		login_btn.setBackground(new Color(220, 220, 220));

		login_btn.setFont(new Font("굴림", Font.BOLD, 13));
		login_btn.setBounds(129, 342, 150, 24);
		Login_UI.getContentPane().add(login_btn);

		id_tf = new JTextField();
		id_tf.setBounds(129, 250, 150, 24);
		login_panel.add(id_tf);
		id_tf.setColumns(10);

		pw_tf = new JPasswordField();
		pw_tf.setBounds(129, 290, 150, 24);
		login_panel.add(pw_tf);

		String imgPath = ".\\images\\icons\\exit.PNG";
		ImageIcon originIcon = new ImageIcon(imgPath);

		Image originImg = originIcon.getImage();
		Image changedImg = originImg.getScaledInstance(45, 40, Image.SCALE_SMOOTH);
		ImageIcon Icon = new ImageIcon(changedImg);

		exit_btn = new JButton("");
		exit_btn.setIcon(new ImageIcon(changedImg));
		exit_btn.setFont(new Font("Century Gothic", Font.BOLD, 8));
		exit_btn.setBounds(372, 0, 30, 30);
		login_panel.add(exit_btn);

		join_lb = new JLabel("\uD68C\uC6D0\uAC00\uC785");
		join_lb.setForeground(Color.WHITE);
		join_lb.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		join_lb.setBounds(230, 376, 63, 29);
		login_panel.add(join_lb);

		String imgPath0 = ".\\images\\icons\\simbol.png";
		ImageIcon originIcon0 = new ImageIcon(imgPath0);

		Image originImg0 = originIcon0.getImage();
		Image changedImg0 = originImg0.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		ImageIcon Icon0 = new ImageIcon(changedImg0);

		JLabel symbol = new JLabel("");
		symbol.setIcon(new ImageIcon(changedImg0));
		symbol.setBounds(150, 140, 90, 90);
		symbol.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				Login_UI.setLocation(x - 200, y - 165);
			}
		});
		login_panel.add(symbol);

		Login_UI.setVisible(true);
	}

	private void MainFrame_init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 650);
		setTitle("Denti-Check");
		setLocationRelativeTo(null);
		String IconPath = ".\\images\\icons\\simbol.PNG";
		ImageIcon SimbolIcon = new ImageIcon(IconPath);
		setIconImage(SimbolIcon.getImage());

		add(mainContentPane);
		mainContentPane.setLayout(card);

		serviceContentPane = new JPanel();
		mainContentPane.add(serviceContentPane);

		serviceContentPane.setBackground(new Color(240, 248, 255));
		serviceContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		serviceContentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 619, 701, -617);
		serviceContentPane.add(scrollPane);

		String imgPath = ".\\images\\icons\\123.PNG";
		ImageIcon originIcon = new ImageIcon(imgPath);

		Image originImg = originIcon.getImage();
		Image changedImg = originImg.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon = new ImageIcon(changedImg);

		menu1_btn = new JButton("");
		menu1_btn.setForeground(SystemColor.window);
		menu1_btn.setBackground(new Color(240, 248, 255));
		menu1_btn.setIcon(new ImageIcon(changedImg));
		menu1_btn.setBorderPainted(false);

		menu1_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		menu1_btn.setBounds(178, 220, 80, 80);
		serviceContentPane.add(menu1_btn);

		String imgPath2 = ".\\images\\icons\\456.PNG";
		ImageIcon originIcon2 = new ImageIcon(imgPath2);

		Image originImg2 = originIcon2.getImage();
		Image changedImg2 = originImg2.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon2 = new ImageIcon(changedImg2);

		menu2_btn = new JButton("");
		menu2_btn.setBackground(new Color(240, 248, 255));
		menu2_btn.setForeground(SystemColor.window);
		menu2_btn.setIcon(new ImageIcon(changedImg2));
		menu2_btn.setBorderPainted(false);
		menu2_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		menu2_btn.setBounds(310, 220, 80, 80);
		serviceContentPane.add(menu2_btn);

		String imgPath3 = ".\\images\\icons\\789.PNG";
		ImageIcon originIcon3 = new ImageIcon(imgPath3);

		Image originImg3 = originIcon3.getImage();
		Image changedImg3 = originImg3.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon3 = new ImageIcon(changedImg3);

		menu3_btn = new JButton("");
		menu3_btn.setBackground(new Color(240, 248, 255));
		menu3_btn.setIcon(new ImageIcon(changedImg3));
		menu3_btn.setForeground(SystemColor.window);
		menu3_btn.setBorderPainted(false);
		menu3_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		menu3_btn.setBounds(450, 220, 80, 80);
		serviceContentPane.add(menu3_btn);

		String imgPath4 = ".\\images\\icons\\789.PNG";
		ImageIcon originIcon4 = new ImageIcon(imgPath4);

		Image originImg4 = originIcon4.getImage();
		Image changedImg4 = originImg4.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon4 = new ImageIcon(changedImg4);

		logout_btn = new JButton("Log Out");
		logout_btn.setBackground(new Color(240, 248, 255));
		logout_btn.setForeground(new Color(0, 0, 128));
		logout_btn.setFont(new Font("경기천년제목 Light", Font.BOLD, 12));
		logout_btn.setBounds(581, 570, 95, 25);
		logout_btn.setBorderPainted(false);
		serviceContentPane.add(logout_btn);

		JLabel Label1 = new JLabel("원격진료");
		Label1.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		Label1.setForeground(new Color(0, 0, 128));
		Label1.setBounds(183, 315, 60, 15);
		serviceContentPane.add(Label1);

		JLabel Label2 = new JLabel("자가진단");
		Label2.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		Label2.setForeground(new Color(0, 0, 128));
		Label2.setBounds(323, 315, 80, 15);
		serviceContentPane.add(Label2);

		JLabel Label3 = new JLabel("치아 건강정보");
		Label3.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		Label3.setForeground(new Color(0, 0, 128));
		Label3.setBounds(440, 315, 105, 15);
		serviceContentPane.add(Label3);

		JLabel Label0 = new JLabel("원하는 서비스를 선택하세요.");
		Label0.setFont(new Font("경기천년제목 Light", Font.BOLD, 15));
		Label0.setBounds(271, 120, 221, 31);
		serviceContentPane.add(Label0);

		serviceContentPane.setVisible(false);
		this.setVisible(false);
	}

	private void selfCheckPanel() {
		selfContentPane = new JPanel();
		mainContentPane.add(selfContentPane);

		selfContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		selfContentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 248, 255));
		panel.setBounds(0, 0, 698, 616);
		selfContentPane.add(panel);
		panel.setLayout(null);

		String imgPath1 = "C:.\\images\\icons\\a.png";
		ImageIcon originIcon1 = new ImageIcon(imgPath1);

		Image originImg1 = originIcon1.getImage();
		Image changedImg1 = originImg1.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon1 = new ImageIcon(changedImg1);

		select1_btn = new JButton("");
		select1_btn.setBackground(new Color(240, 248, 255));
		select1_btn.setIcon(new ImageIcon(changedImg1));
		select1_btn.setBorderPainted(false);
		select1_btn.setBounds(86, 200, 70, 80);
		panel.add(select1_btn);

		String imgPath2 = "C:.\\images\\icons\\b.png";
		ImageIcon originIcon2 = new ImageIcon(imgPath2);

		Image originImg2 = originIcon2.getImage();
		Image changedImg2 = originImg2.getScaledInstance(80, 70, Image.SCALE_SMOOTH);
		ImageIcon Icon2 = new ImageIcon(changedImg2);

		select2_btn = new JButton("");
		select2_btn.setBackground(new Color(240, 248, 255));
		select2_btn.setIcon(new ImageIcon(changedImg2));
		select2_btn.setBorderPainted(false);
		select2_btn.setBounds(198, 200, 70, 80);
		panel.add(select2_btn);

		String imgPath3 = "C:.\\images\\icons\\c.png";
		ImageIcon originIcon3 = new ImageIcon(imgPath3);

		Image originImg3 = originIcon3.getImage();
		Image changedImg3 = originImg3.getScaledInstance(70, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon3 = new ImageIcon(changedImg3);

		select3_btn = new JButton("");
		select3_btn.setBackground(new Color(240, 248, 255));
		select3_btn.setIcon(new ImageIcon(changedImg3));
		select3_btn.setBorderPainted(false);
		select3_btn.setBounds(315, 200, 70, 80);
		panel.add(select3_btn);

		String imgPath4 = "C:.\\images\\icons\\d.png";
		ImageIcon originIcon4 = new ImageIcon(imgPath4);

		Image originImg4 = originIcon4.getImage();
		Image changedImg4 = originImg4.getScaledInstance(70, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon4 = new ImageIcon(changedImg4);

		select4_btn = new JButton("");
		select4_btn.setBackground(new Color(240, 248, 255));
		select4_btn.setIcon(new ImageIcon(changedImg4));
		select4_btn.setBorderPainted(false);
		select4_btn.setBounds(424, 200, 70, 80);
		panel.add(select4_btn);

		String imgPath5 = "C:.\\images\\icons\\e.png";
		ImageIcon originIcon5 = new ImageIcon(imgPath5);

		Image originImg5 = originIcon5.getImage();
		Image changedImg5 = originImg5.getScaledInstance(80, 70, Image.SCALE_SMOOTH);
		ImageIcon Icon5 = new ImageIcon(changedImg5);

		select5_btn = new JButton("");
		select5_btn.setBackground(new Color(240, 248, 255));
		select5_btn.setIcon(new ImageIcon(changedImg5));
		select5_btn.setBorderPainted(false);
		select5_btn.setBounds(536, 200, 70, 80);
		panel.add(select5_btn);

		back_btn = new JButton("Back");
		back_btn.setBackground(new Color(0, 0, 128));
		back_btn.setBackground(new Color(240, 248, 255));
		back_btn.setBounds(22, 565, 80, 25);
		panel.add(back_btn);

		String imgPath7 = ".\\images\\icons\\Camera.png";
		ImageIcon originIcon7 = new ImageIcon(imgPath7);

		Image originImg7 = originIcon7.getImage();
		Image changedImg7 = originImg7.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		ImageIcon Icon7 = new ImageIcon(changedImg7);

		camera_btn = new JButton("");
		camera_btn.setBackground(new Color(240, 248, 255));
		camera_btn.setIcon(new ImageIcon(changedImg7));
		camera_btn.setBorderPainted(false);
		camera_btn.setBounds(315, 358, 70, 70);
		panel.add(camera_btn);

		label1_lb = new JLabel("충치");
		label1_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label1_lb.setForeground(new Color(0, 0, 128));
		label1_lb.setBounds(101, 290, 55, 15);
		panel.add(label1_lb);

		label2_lb = new JLabel("잇몸시림");
		label2_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label2_lb.setForeground(new Color(0, 0, 128));
		label2_lb.setBounds(200, 290, 62, 15);
		panel.add(label2_lb);

		label3_lb = new JLabel("부정교합");
		label3_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label3_lb.setForeground(new Color(0, 0, 128));
		label3_lb.setBounds(318, 290, 62, 15);
		panel.add(label3_lb);

		label4_lb = new JLabel("악관절증");
		label4_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label4_lb.setForeground(new Color(0, 0, 128));
		label4_lb.setBounds(434, 290, 62, 15);
		panel.add(label4_lb);

		label5_lb = new JLabel("구내염");
		label5_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label5_lb.setForeground(new Color(0, 0, 128));
		label5_lb.setBounds(552, 290, 50, 15);
		panel.add(label5_lb);

		label0_lb = new JLabel("궁금하신 질병을 확인해 보세요.");
		label0_lb.setFont(new Font("경기천년제목 Light", Font.BOLD, 15));
		label0_lb.setBounds(256, 105, 221, 31);
		panel.add(label0_lb);

		selfContentPane.setVisible(false);
	}

	private void informationPanel() {
		infoContentPane = new JPanel();
		mainContentPane.add(infoContentPane);

		infoContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		infoContentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 248, 255));
		panel.setBounds(0, 0, 698, 616);
		infoContentPane.add(panel);
		panel.setLayout(null);

		String imgPath1 = "C:.\\images\\icons\\aa.png";
		ImageIcon originIcon1 = new ImageIcon(imgPath1);

		Image originImg1 = originIcon1.getImage();
		Image changedImg1 = originImg1.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon1 = new ImageIcon(changedImg1);

		select3_1_btn = new JButton("");
		select3_1_btn.setBackground(new Color(240, 248, 255));
		select3_1_btn.setIcon(new ImageIcon(changedImg1));
		select3_1_btn.setBorderPainted(false);
		select3_1_btn.setBounds(139, 168, 80, 80);
		panel.add(select3_1_btn);

		String imgPath2 = "C:.\\images\\icons\\bb.png";
		ImageIcon originIcon2 = new ImageIcon(imgPath2);

		Image originImg2 = originIcon2.getImage();
		Image changedImg2 = originImg2.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon2 = new ImageIcon(changedImg2);

		select3_2_btn = new JButton("");
		select3_2_btn.setBackground(new Color(240, 248, 255));
		select3_2_btn.setIcon(new ImageIcon(changedImg2));
		select3_2_btn.setBorderPainted(false);
		select3_2_btn.setBounds(303, 168, 80, 80);
		panel.add(select3_2_btn);

		String imgPath3 = "C:.\\images\\icons\\cc.png";
		ImageIcon originIcon3 = new ImageIcon(imgPath3);

		Image originImg3 = originIcon3.getImage();
		Image changedImg3 = originImg3.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon3 = new ImageIcon(changedImg3);

		select3_3_btn = new JButton("");
		select3_3_btn.setBackground(new Color(240, 248, 255));
		select3_3_btn.setIcon(new ImageIcon(changedImg3));
		select3_3_btn.setBorderPainted(false);
		select3_3_btn.setBounds(450, 168, 80, 80);
		panel.add(select3_3_btn);

		String imgPath4 = "C:.\\images\\icons\\dd.png";
		ImageIcon originIcon4 = new ImageIcon(imgPath4);

		Image originImg4 = originIcon4.getImage();
		Image changedImg4 = originImg4.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon4 = new ImageIcon(changedImg4);

		select3_4_btn = new JButton("");
		select3_4_btn.setBackground(new Color(240, 248, 255));
		select3_4_btn.setIcon(new ImageIcon(changedImg4));
		select3_4_btn.setBorderPainted(false);
		select3_4_btn.setBounds(139, 299, 80, 80);
		panel.add(select3_4_btn);

		String imgPath5 = "C:.\\images\\icons\\ee.png";
		ImageIcon originIcon5 = new ImageIcon(imgPath5);

		Image originImg5 = originIcon5.getImage();
		Image changedImg5 = originImg5.getScaledInstance(90, 80, Image.SCALE_SMOOTH);
		ImageIcon Icon5 = new ImageIcon(changedImg5);

		select3_5_btn = new JButton("");
		select3_5_btn.setBackground(new Color(240, 248, 255));
		select3_5_btn.setIcon(new ImageIcon(changedImg5));
		select3_5_btn.setBorderPainted(false);
		select3_5_btn.setBounds(303, 299, 80, 80);
		panel.add(select3_5_btn);

		String imgPath6 = "C:.\\images\\icons\\ff.png";
		ImageIcon originIcon6 = new ImageIcon(imgPath6);

		Image originImg6 = originIcon6.getImage();
		Image changedImg6 = originImg6.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		ImageIcon Icon6 = new ImageIcon(changedImg6);

		select3_6_btn = new JButton("");
		select3_6_btn.setBackground(new Color(240, 248, 255));
		select3_6_btn.setIcon(new ImageIcon(changedImg6));
		select3_6_btn.setBorderPainted(false);
		select3_6_btn.setBounds(450, 299, 80, 80);
		panel.add(select3_6_btn);

		back_2_btn = new JButton("Back");
		back_2_btn.setForeground(new Color(0, 0, 128));
		back_2_btn.setBackground(new Color(240, 248, 255));

		back_2_btn.setBounds(22, 565, 80, 25);
		panel.add(back_2_btn);

		label3_1_lb = new JLabel("올바른 양치법");
		label3_1_lb.setForeground(new Color(0, 0, 128));
		label3_1_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label3_1_lb.setBounds(135, 250, 95, 23);
		panel.add(label3_1_lb);

		label3_2_lb = new JLabel("나이대별 치아 관리");
		label3_2_lb.setForeground(new Color(0, 0, 128));
		label3_2_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label3_2_lb.setBounds(277, 250, 130, 23);
		panel.add(label3_2_lb);

		label3_3_lb = new JLabel("구취 예방법");
		label3_3_lb.setForeground(new Color(0, 0, 128));
		label3_3_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label3_3_lb.setBounds(451, 250, 80, 23);
		panel.add(label3_3_lb);

		label3_4_lb = new JLabel("셀프 미백 방법");
		label3_4_lb.setForeground(new Color(0, 0, 128));
		label3_4_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label3_4_lb.setBounds(133, 384, 100, 23);
		panel.add(label3_4_lb);

		label3_5_lb = new JLabel("치아에 좋은 음식");
		label3_5_lb.setForeground(new Color(0, 0, 128));
		label3_5_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label3_5_lb.setBounds(284, 384, 119, 23);
		panel.add(label3_5_lb);

		label3_6_lb = new JLabel("치아에 안좋은 음식");
		label3_6_lb.setForeground(new Color(0, 0, 128));
		label3_6_lb.setFont(new Font("경기천년제목 Medium", Font.BOLD, 15));
		label3_6_lb.setBounds(433, 384, 145, 23);
		panel.add(label3_6_lb);

		label3_0_lb = new JLabel("궁금하신 정보를 확인해 보세요.");
		label3_0_lb.setFont(new Font("경기천년제목 Light", Font.BOLD, 15));
		label3_0_lb.setBounds(241, 105, 221, 31);
		panel.add(label3_0_lb);

		infoContentPane.setVisible(false);
	}

	private void findDentistPanel() {
		findContentPane = new JPanel();
		mainContentPane.add(findContentPane);

		findContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		findContentPane.setBackground(new Color(240, 248, 255));
		findContentPane.setLayout(null);

		back_1_btn = new JButton("Back");
		back_1_btn.setForeground(new Color(0, 0, 128));
		back_1_btn.setBackground(new Color(240, 248, 255));
		back_1_btn.setBounds(22, 565, 80, 25);
		findContentPane.add(back_1_btn);

		ok1_btn = new JButton("검색");
		ok1_btn.setForeground(new Color(0, 0, 0));
		ok1_btn.setBackground(SystemColor.activeCaption);
//		ok1_btn.setForeground(new Color(0, 0, 128));
//		ok1_btn.setBackground(new Color(240, 248, 255));
		ok1_btn.setBounds(390, 210, 95, 23);
		findContentPane.add(ok1_btn);

		ok2_btn = new JButton("검색");
		ok2_btn.setForeground(new Color(0, 0, 0));
		ok2_btn.setBackground(SystemColor.activeCaption);
//		ok2_btn.setForeground(new Color(0, 0, 128));
//		ok2_btn.setBackground(new Color(240, 248, 255));
		ok2_btn.setBounds(390, 335, 95, 23);
		findContentPane.add(ok2_btn);

		JLabel lblNewLabel = new JLabel("이름으로 검색");
		lblNewLabel.setBounds(235, 180, 146, 15);
		findContentPane.add(lblNewLabel);

		JLabel label = new JLabel("지역으로 검색");
		label.setBounds(235, 270, 146, 15);
		findContentPane.add(label);

		select_tf = new JTextField();
		select_tf.setBounds(235, 210, 131, 23);
		findContentPane.add(select_tf);
		select_tf.setColumns(10);

		do_cb = new JComboBox();
		do_cb.setBounds(235, 300, 131, 23);
		do_cb.addItem("선택");
		do_cb.addItem("서울특별시");
		findContentPane.add(do_cb);

		gu_cb = new JComboBox();
		gu_cb.setBounds(235, 335, 131, 23);
		gu_cb.addItem("선택");
		gu_cb.addItem("노원구");
		gu_cb.addItem("동대문구");
		findContentPane.add(gu_cb);

		JLabel Label3 = new JLabel("도");
		Label3.setBounds(210, 305, 25, 15);
		findContentPane.add(Label3);

		JLabel label_1 = new JLabel("구");
		label_1.setBounds(210, 340, 25, 15);
		findContentPane.add(label_1);

		findContentPane.setVisible(false);
	}

	private void roomListPanel() {
		listContentPane = new JPanel();
		mainContentPane.add(listContentPane);

		listContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		listContentPane.setBackground(new Color(240, 248, 255));

		JLabel lblNewLabel = new JLabel("치과 목록");
		lblNewLabel.setBounds(19, 17, 198, 43);
		lblNewLabel.setFont(new Font("경기천년제목 bold", Font.BOLD, 23));

		listScrollPane = new JScrollPane();
		listScrollPane.setBounds(19, 67, 640, 485);

		dentistList = new JList();
		dentistList.setFont(new Font("맑은 고딕", Font.BOLD, 23));
		dentistList.setListData(rooms);
		;
		listScrollPane.setViewportView(dentistList);

		care_btn = new JButton("진료 받기");
		care_btn.setBackground(new Color(0, 0, 128));
		care_btn.setBackground(new Color(240, 248, 255));
		care_btn.setBounds(562, 565, 92, 25);

		back_3_btn = new JButton("Back");
		back_3_btn.setBackground(new Color(0, 0, 128));
		back_3_btn.setBackground(new Color(240, 248, 255));
		back_3_btn.setBounds(22, 565, 80, 25);

		listContentPane.setLayout(null);
		listContentPane.add(care_btn);
		listContentPane.add(lblNewLabel);
		listContentPane.add(listScrollPane);
		listContentPane.add(back_3_btn);

		listContentPane.setVisible(false);
	}

	private void chattingPanel() {
		ChattingContentPane = new JPanel();
		mainContentPane.add(ChattingContentPane);

		ChattingContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		ChattingContentPane.setBackground(new Color(240, 248, 255));
		ChattingContentPane.setLayout(null);

		write_tf = new JTextField();
		write_tf.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		write_tf.setBounds(14, 554, 555, 27);
		ChattingContentPane.add(write_tf);
		write_tf.setColumns(10);

		sendMsg_btn = new JButton("전송");
		sendMsg_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		sendMsg_btn.setBounds(580, 554, 89, 27);
		sendMsg_btn.setForeground(new Color(0, 0, 0));
		sendMsg_btn.setBackground(SystemColor.activeCaption);
		ChattingContentPane.add(sendMsg_btn);

		sendPhoto_btn = new JButton("사진 전송");
		sendPhoto_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		sendPhoto_btn.setBounds(114, 12, 99, 27);
		sendPhoto_btn.setForeground(new Color(0, 0, 0));
		sendPhoto_btn.setBackground(SystemColor.activeCaption);
		ChattingContentPane.add(sendPhoto_btn);

		camera_2_btn = new JButton("카메라");
		camera_2_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CamCap cam = new CamCap();
				cam.setVisible(true);
			}
		});
		camera_2_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		camera_2_btn.setBounds(14, 12, 89, 27);
		camera_2_btn.setForeground(new Color(0, 0, 0));
		camera_2_btn.setBackground(SystemColor.activeCaption);
		ChattingContentPane.add(camera_2_btn);

		quitChat_btn = new JButton("상담 종료");
		quitChat_btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		quitChat_btn.setBounds(570, 12, 99, 27);
		quitChat_btn.setForeground(new Color(0, 0, 0));
		quitChat_btn.setBackground(SystemColor.activeCaption);
		ChattingContentPane.add(quitChat_btn);

		chatScrollPane = new JScrollPane();
		chatScrollPane.setBounds(14, 51, 655, 491);
		ChattingContentPane.add(chatScrollPane);

		msg_ta = new JTextArea();
		msg_ta.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		msg_ta.setEditable(false);
		chatScrollPane.setViewportView(msg_ta);

		ChattingContentPane.setVisible(false);
	}

	private void chattingForDenPanel() {
		ChattingContentPane_D = new JPanel();
		mainContentPane.add(ChattingContentPane_D);

		ChattingContentPane_D.setBorder(new EmptyBorder(5, 5, 5, 5));
		ChattingContentPane_D.setBackground(new Color(240, 248, 255));
		ChattingContentPane_D.setLayout(null);

		write_tf_D = new JTextField();
		write_tf_D.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		write_tf_D.setBounds(14, 554, 555, 27);
		ChattingContentPane_D.add(write_tf_D);
		write_tf_D.setColumns(10);
		write_tf_D.setEditable(false);

		sendMsg_btn_D = new JButton("전송");
		sendMsg_btn_D.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		sendMsg_btn_D.setBounds(580, 554, 89, 27);
		sendMsg_btn_D.setForeground(new Color(0, 0, 0));
		sendMsg_btn_D.setBackground(SystemColor.activeCaption);
		ChattingContentPane_D.add(sendMsg_btn_D);
		sendMsg_btn_D.setEnabled(false);

		sendPhoto_btn_D = new JButton("사진 전송");
		sendPhoto_btn_D.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		sendPhoto_btn_D.setBounds(14, 12, 99, 27);
		sendPhoto_btn_D.setForeground(new Color(0, 0, 0));
		sendPhoto_btn_D.setBackground(SystemColor.activeCaption);
		ChattingContentPane_D.add(sendPhoto_btn_D);
		sendPhoto_btn_D.setEnabled(false);

		startChat_btn_D = new JButton("상담 시작");
		startChat_btn_D.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		startChat_btn_D.setBounds(450, 12, 100, 27);
		startChat_btn_D.setForeground(new Color(0, 0, 0));
		startChat_btn_D.setBackground(SystemColor.activeCaption);
		ChattingContentPane_D.add(startChat_btn_D);

		quitChat_btn_D = new JButton("상담 종료");
		quitChat_btn_D.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		quitChat_btn_D.setBounds(570, 12, 99, 27);
		quitChat_btn_D.setForeground(new Color(0, 0, 0));
		quitChat_btn_D.setBackground(SystemColor.activeCaption);
		ChattingContentPane_D.add(quitChat_btn_D);
		quitChat_btn_D.setEnabled(false);

		chatScrollPane_D = new JScrollPane();
		chatScrollPane_D.setBounds(14, 51, 655, 491);
		ChattingContentPane_D.add(chatScrollPane_D);

		msg_ta_D = new JTextArea();
		msg_ta_D.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		msg_ta_D.setEditable(false);
		chatScrollPane_D.setViewportView(msg_ta_D);

		ChattingContentPane_D.setVisible(false);
	}

	private void joinMemCheckFrame() {
		joinMemCheck_ui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		joinMemCheck_ui.setBounds(100, 100, 321, 177);
		joinMemCheck_ui.setLocationRelativeTo(null);
		String IconPath = ".\\images\\icons\\simbol.PNG";
		ImageIcon SimbolIcon = new ImageIcon(IconPath);
		joinMemCheck_ui.setIconImage(SimbolIcon.getImage());
		MemCheckContentPane = new JPanel();
		MemCheckContentPane.setBackground(new Color(240, 248, 255));
		MemCheckContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		joinMemCheck_ui.setContentPane(MemCheckContentPane);
		MemCheckContentPane.setLayout(null);

		JLabel Check = new JLabel("\uAD6C\uBD84\uC744 \uC120\uD0DD\uD574 \uC8FC\uC2ED\uC2DC\uC624.");
		Check.setFont(new Font("경기천년제목 Light", Font.PLAIN, 15));
		Check.setBounds(85, 27, 172, 15);
		MemCheckContentPane.add(Check);

		select_clients_btn = new JButton("\uAC1C\uC778");
		select_clients_btn.setFont(new Font("경기천년제목 Light", Font.PLAIN, 12));
		select_clients_btn.setBackground(new Color(248, 248, 255));
		select_clients_btn.setBounds(34, 71, 95, 23);
		MemCheckContentPane.add(select_clients_btn);

		select_clinics_btn = new JButton("\uCE58\uACFC");
		select_clinics_btn.setBorderPainted(true);
		select_clinics_btn.setFont(new Font("경기천년제목 Light", Font.PLAIN, 12));
		select_clinics_btn.setBackground(new Color(248, 248, 255));
		select_clinics_btn.setBounds(172, 71, 95, 23);
		MemCheckContentPane.add(select_clinics_btn);

		joinMemCheck_ui.setVisible(false);
	}

	private void joinClientFrame() {
		joinMemClient_ui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		joinMemClient_ui.setBounds(100, 100, 400, 600);
		joinMemClient_ui.setLocationRelativeTo(null);
		joinMemClient_ui.setTitle("회원가입");
		String IconPath = ".\\images\\icons\\simbol.PNG";
		ImageIcon SimbolIcon = new ImageIcon(IconPath);
		joinMemClient_ui.setIconImage(SimbolIcon.getImage());
		JoinClientContentPane = new JPanel();
		JoinClientContentPane.setBackground(SystemColor.activeCaption);
		JoinClientContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		joinMemClient_ui.setContentPane(JoinClientContentPane);
		JoinClientContentPane.setLayout(null);

		JLabel LabelWelcome = new JLabel("WELCOME :)");
		LabelWelcome.setForeground(SystemColor.window);
		LabelWelcome.setFont(new Font("굴림", Font.PLAIN, 16));
		LabelWelcome.setBounds(149, 62, 113, 29);
		JoinClientContentPane.add(LabelWelcome);

		JSeparator separator = new JSeparator();
		separator.setBounds(42, 101, 284, 2);
		JoinClientContentPane.add(separator);

		JLabel LabelID = new JLabel("\uC544\uC774\uB514");
		LabelID.setForeground(SystemColor.window);
		LabelID.setFont(new Font("경기천년제목 Light", Font.PLAIN, 13));
		LabelID.setBounds(104, 140, 48, 29);
		JoinClientContentPane.add(LabelID);

		JLabel LabelPW = new JLabel("\uBE44\uBC00\uBC88\uD638");
		LabelPW.setForeground(SystemColor.window);
		LabelPW.setFont(new Font("경기천년제목 Light", Font.PLAIN, 13));
		LabelPW.setBounds(95, 179, 63, 29);
		JoinClientContentPane.add(LabelPW);

		JLabel labelRepeatPW = new JLabel("\uBE44\uBC00\uBC88\uD638 \uD655\uC778");
		labelRepeatPW.setForeground(SystemColor.window);
		labelRepeatPW.setFont(new Font("경기천년제목 Light", Font.PLAIN, 13));
		labelRepeatPW.setBounds(61, 218, 97, 29);
		JoinClientContentPane.add(labelRepeatPW);

		JLabel labelName = new JLabel("\uC774\uB984");
		labelName.setForeground(SystemColor.window);
		labelName.setFont(new Font("경기천년제목 Light", Font.PLAIN, 13));
		labelName.setBounds(114, 280, 38, 29);
		JoinClientContentPane.add(labelName);

		JLabel labelNum = new JLabel("\uC804\uD654\uBC88\uD638");
		labelNum.setForeground(SystemColor.window);
		labelNum.setFont(new Font("경기천년제목 Light", Font.PLAIN, 13));
		labelNum.setBounds(89, 320, 63, 29);
		JoinClientContentPane.add(labelNum);

		JLabel labelBirth = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
		labelBirth.setForeground(SystemColor.window);
		labelBirth.setFont(new Font("경기천년제목 Light", Font.PLAIN, 13));
		labelBirth.setBounds(89, 360, 63, 29);
		JoinClientContentPane.add(labelBirth);

		join_clients_btn = new JButton("\uD68C\uC6D0\uAC00\uC785");
		join_clients_btn.setFont(new Font("경기천년제목 Light", Font.PLAIN, 12));
		join_clients_btn.setBackground(new Color(220, 220, 220));
		join_clients_btn.setForeground(new Color(0, 0, 0));
		join_clients_btn.setBounds(138, 435, 124, 29);
		JoinClientContentPane.add(join_clients_btn);

		id_clients_tf = new JTextField();
		id_clients_tf.setBounds(150, 140, 124, 23);
		JoinClientContentPane.add(id_clients_tf);
		id_clients_tf.setColumns(10);

		pw_clients_pf = new JPasswordField();
		pw_clients_pf.setBounds(150, 180, 124, 23);
		JoinClientContentPane.add(pw_clients_pf);

		repeatpw_clients_pf = new JPasswordField();
		repeatpw_clients_pf.setBounds(150, 220, 124, 23);
		JoinClientContentPane.add(repeatpw_clients_pf);

		name_clients_tf = new JTextField();
		name_clients_tf.setColumns(10);
		name_clients_tf.setBounds(150, 280, 124, 23);
		JoinClientContentPane.add(name_clients_tf);

		num_clients_tf = new JTextField();
		num_clients_tf.setColumns(10);
		num_clients_tf.setBounds(150, 320, 124, 23);
		JoinClientContentPane.add(num_clients_tf);

		birth_clients_tf = new JTextField();
		birth_clients_tf.setColumns(10);
		birth_clients_tf.setBounds(150, 360, 124, 23);
		JoinClientContentPane.add(birth_clients_tf);

		joinMemClient_ui.setVisible(false);
	}

	private void joinClinicFrame() {
		joinMemClinic_ui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		joinMemClinic_ui.setBounds(100, 100, 400, 600);
		joinMemClinic_ui.setLocationRelativeTo(null);
		joinMemClinic_ui.setTitle("회원가입");
		String IconPath = ".\\images\\icons\\simbol.PNG";
		ImageIcon SimbolIcon = new ImageIcon(IconPath);
		joinMemClinic_ui.setIconImage(SimbolIcon.getImage());
		JoinClinicContentPane = new JPanel();
		JoinClinicContentPane.setBackground(SystemColor.activeCaption);
		JoinClinicContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		joinMemClinic_ui.setContentPane(JoinClinicContentPane);
		JoinClinicContentPane.setLayout(null);

		JLabel LabelWelcome = new JLabel("WELCOME :)");
		LabelWelcome.setForeground(SystemColor.window);
		LabelWelcome.setFont(new Font("굴림", Font.PLAIN, 16));
		LabelWelcome.setBounds(149, 62, 113, 29);
		JoinClinicContentPane.add(LabelWelcome);

		JSeparator separator = new JSeparator();
		separator.setBounds(42, 101, 284, 2);
		JoinClinicContentPane.add(separator);

		JLabel LabelID = new JLabel("\uC544\uC774\uB514");
		LabelID.setForeground(SystemColor.window);
		LabelID.setFont(new Font("경기천년제목 Light", Font.PLAIN, 12));
		LabelID.setBounds(104, 140, 48, 29);
		JoinClinicContentPane.add(LabelID);

		JLabel LabelPW = new JLabel("\uBE44\uBC00\uBC88\uD638");
		LabelPW.setForeground(SystemColor.window);
		LabelPW.setFont(new Font("경기천년제목 Light", Font.PLAIN, 12));
		LabelPW.setBounds(95, 179, 63, 29);
		JoinClinicContentPane.add(LabelPW);

		JLabel labelRepeatPW = new JLabel("\uBE44\uBC00\uBC88\uD638 \uD655\uC778");
		labelRepeatPW.setForeground(SystemColor.window);
		labelRepeatPW.setFont(new Font("경기천년제목 Light", Font.PLAIN, 12));
		labelRepeatPW.setBounds(71, 218, 97, 29);
		JoinClinicContentPane.add(labelRepeatPW);

		JLabel labelName = new JLabel("\uCE58\uACFC\uBA85");
		labelName.setForeground(SystemColor.window);
		labelName.setFont(new Font("경기천년제목 Light", Font.PLAIN, 12));
		labelName.setBounds(100, 281, 38, 29);
		JoinClinicContentPane.add(labelName);

		JLabel labelNum = new JLabel("\uC804\uD654\uBC88\uD638");
		labelNum.setForeground(SystemColor.window);
		labelNum.setFont(new Font("경기천년제목 Light", Font.PLAIN, 12));
		labelNum.setBounds(89, 320, 63, 29);
		JoinClinicContentPane.add(labelNum);

		JLabel labelAddress = new JLabel("\uC8FC\uC18C");
		labelAddress.setForeground(SystemColor.window);
		labelAddress.setFont(new Font("경기천년제목 Light", Font.PLAIN, 12));
		labelAddress.setBounds(104, 358, 32, 29);
		JoinClinicContentPane.add(labelAddress);

		join_clinics_btn = new JButton("\uD68C\uC6D0\uAC00\uC785");
		join_clinics_btn.setFont(new Font("경기천년제목 Light", Font.PLAIN, 12));
		join_clinics_btn.setBackground(new Color(220, 220, 220));
		join_clinics_btn.setBounds(150, 420, 124, 29);
		JoinClinicContentPane.add(join_clinics_btn);

		id_clinics_tf = new JTextField();
		id_clinics_tf.setBounds(150, 140, 124, 23);
		JoinClinicContentPane.add(id_clinics_tf);
		id_clinics_tf.setColumns(10);

		pw_clinics_pf = new JPasswordField();
		pw_clinics_pf.setBounds(150, 180, 124, 23);
		JoinClinicContentPane.add(pw_clinics_pf);

		repeatpw_clinics_pf = new JPasswordField();
		repeatpw_clinics_pf.setBounds(150, 220, 124, 23);
		JoinClinicContentPane.add(repeatpw_clinics_pf);

		name_clinics_tf = new JTextField();
		name_clinics_tf.setColumns(10);
		name_clinics_tf.setBounds(150, 280, 124, 23);
		JoinClinicContentPane.add(name_clinics_tf);

		num_clinics_tf = new JTextField();
		num_clinics_tf.setColumns(10);
		num_clinics_tf.setBounds(150, 320, 124, 23);
		JoinClinicContentPane.add(num_clinics_tf);

		address_clinics_tf = new JTextField();
		address_clinics_tf.setColumns(10);
		address_clinics_tf.setBounds(150, 360, 124, 23);
		JoinClinicContentPane.add(address_clinics_tf);

		joinMemClinic_ui.setVisible(false);
	}

	private void Network() {
		try {
			socket = new Socket(ip, port);

			if (socket != null) { // 정상적으로 소켓이 연결되었을 경우
				Connection();
			}

		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "서버 연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "서버 연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void Connection() {
		try {
			is = socket.getInputStream();
			dis = new DataInputStream(is);

			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "서버 연결 실패", "알림", JOptionPane.ERROR_MESSAGE);
		}

		this.setVisible(true); // main ui 표시
		if (checkCilent == 1) { // 일반 고객
			serviceContentPane.setVisible(true);
		} else { // 치과
			serviceContentPane.setVisible(false);
			ChattingContentPane_D.setVisible(true);
		}
		Login_UI.setVisible(false);

		// 처음 접속시에 ID 전송
		send_message(id);

		// users(vector)에 사용자 추가
		users.add(id);

		// 메세지 송수신을 다른 스레드로 무한루프를 돌며 수행
		Thread th = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						String msg = dis.readUTF();
						System.out.println("서버로부터 수신된 메세지: " + msg);

						inMessage(msg);
					} catch (IOException e) {
						try {
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
							JOptionPane.showMessageDialog(null, "로그아웃 되었습니다.", "알림", JOptionPane.NO_OPTION);
						} catch (IOException e1) {
						}
						break;
					}
				}
			}
		});

		th.start();

	}

	// 서버로부터 메시지를 받아 프로토콜에 따라 동작시킴
	private void inMessage(String str) {
		st = new StringTokenizer(str, "|");

		String protocol = st.nextToken();
		String message = st.nextToken();

		if (protocol.equals("NewUser")) { // 새로운 접속자
			users.add(message);

		} else if (protocol.equals("OldUser")) {
			users.add(message);

		} else if (protocol.equals("CreateRoom")) { // 방을 만들었을 때
			My_Room = message; // 내가 속한 방 이름 입력
			msg_ta_D.append("< " + message + " 방을 생성하였습니다.>\n");
			startChat_btn_D.setEnabled(false);
			write_tf_D.setEditable(true);
			sendMsg_btn_D.setEnabled(true);
			sendPhoto_btn_D.setEnabled(true);
			quitChat_btn_D.setEnabled(true);

		} else if (protocol.equals("CreateRoomFail")) { // 방 만들기 실패했을 때
			JOptionPane.showMessageDialog(null, "방 만들기 실패", "알림", JOptionPane.ERROR_MESSAGE);

		} else if (protocol.equals("New_Room")) { // 새로운 방이 생성 되었을 때
			rooms.add(message);
			dentistList.setListData(rooms);

		} else if (protocol.equals("Chatting")) { // 방에서 채팅을 받음
			String msg = st.nextToken();

			if (checkCilent == 1) {
				if(message.equals(id)) {
					message = "나";
				}
				msg_ta.append(" " + message + " : " + msg + "\n");
			}
			else
				msg_ta_D.append(" " + message + ": " + msg + "\n");

		} else if (protocol.equals("OldRoom")) { // 기존 채팅 방 리스트 추가
			rooms.add(message);

		} else if (protocol.equals("room_list_update")) { // 채팅방 리스트 업데이트
			dentistList.setListData(rooms);

		} else if (protocol.equals("JoinRoom")) {
			My_Room = message;

			if (checkCilent == 1)
				msg_ta.append("\t          " + message + " 입장, 진료를 시작합니다.\n");
			else
				msg_ta_D.append("< " + message + " 에 입장하였습니다.>\n");

			JOptionPane.showMessageDialog(null, message + " 진료를 시작합니다.", "알림", JOptionPane.INFORMATION_MESSAGE);

		} else if (protocol.equals("User_out")) { // 다른 유저가 접속 종료시
			users.remove(message);

		} else if (protocol.equals("Room_out")) { // 방 나가기
			My_Room = "";

			if (checkCilent == 1) {
				msg_ta.setText("");
				write_tf.setText("");
			} else {
				msg_ta_D.setText("");
				write_tf_D.setText("");
			}

			startChat_btn_D.setEnabled(true);
			write_tf_D.setEditable(false);
			sendMsg_btn_D.setEnabled(false);
			sendPhoto_btn_D.setEnabled(false);
			quitChat_btn_D.setEnabled(false);
			JOptionPane.showMessageDialog(null, "진료를 종료하였습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);

		} else if (protocol.equals("Notifying")) { // 방에 공지
			if (checkCilent == 1)
				msg_ta.append("******* " + message + " *******\n");
			else
				msg_ta_D.append("******* " + message + " *******\n");

		} else if (protocol.equals("Room_remove")) { // 방 삭제
			rooms.remove(message);

		} else if (protocol.equals("RequestSendFile")) { // 파일 전송 수락 요청
			String msg = st.nextToken();

			int choice = JOptionPane.showOptionDialog(null, message + msg + "\n파일을 받으시겠습니까?", "파일 전송 수락",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, "yes");

			if (choice == 0) {
				System.out.println("파일 전송 수락");
				send_message("FileAccept|" + My_Room);
			} else {
				System.out.println("파일 전송 거절");
				send_message("FileRefuse|" + My_Room);
			}

		} else if (protocol.equals("SendFile")) { // 파일 전송 시작
			new FileSender(9990, filePath).start(); // 9990 포트로 파일 전송 스레드 시작

			send_message("FileReceiver|" + My_Room);
			/////////////////////////////////////////////////
			/////////////////////////////////////////////////
			filePath = "";

		} else if (protocol.equals("KeepFile")) { // 파일 전송 중단
			if (checkCilent == 1)
				msg_ta.append("*** " + message + " ***\n");
			else
				msg_ta_D.append("*** " + message + " ***\n");

			filePath = "";

		} else if (protocol.equals("ReceiveFile")) { // 파일을 받는다
			String fname = st.nextToken();
			new FileReceiver(message, 9990, fname).start();
			send_message("FileComplete|" + My_Room);
			
		} else if (protocol.equals("FileEnd")) {
			if (checkCilent == 1)
				msg_ta.append("*** 파일 전송이 완료되었습니다 ***\n");
			else
				msg_ta_D.append("*** 파일 전송이 완료되었습니다 ***\n");
			
		}

		chatScrollPane.getVerticalScrollBar().setValue(chatScrollPane.getVerticalScrollBar().getMaximum()); // 자동 스크롤
		chatScrollPane_D.getVerticalScrollBar().setValue(chatScrollPane_D.getVerticalScrollBar().getMaximum()); // 자동
																												// 스크롤

	}

	// 서버에게 메세지를 보내는 메소드
	private void send_message(String str) {
		try {
			dos.writeUTF(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new Main();
	}

	// --------------------- 버튼 리스너 ---------------------
	@Override
	public void actionPerformed(ActionEvent e) {

		JButton b = (JButton) e.getSource();

		// 로그인 화면 // Denti_UI
		if (b == exit_btn) { // 종료 버튼
			System.exit(0);
		} else if (b == login_btn) { // 로그인 버튼

			if (id_tf.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "ID 를 입력해 주세요", "알림", JOptionPane.ERROR_MESSAGE);
			} else if (pw_tf.getPassword().length == 0) {
				JOptionPane.showMessageDialog(null, "PassWord 를 입력해 주세요", "알림", JOptionPane.ERROR_MESSAGE);
			} else {
				id = id_tf.getText().trim();
				char[] pass = pw_tf.getPassword();
				password = new String(pass);

				// 회원 확인 필요
				///////////////
				String availDenList = "den,광운치과,이편한치과,치카치과,미치과,뉴욕치과,빼어날수치과";
				if (availDenList.contains(id))
					checkCilent = 2;
				///////////////

				System.out.println(id + checkCilent);

				id_tf.setText("");
				pw_tf.setText("");

				Network();
			}

		}
		// 서비스 선택 화면 // Denti_UI2_0
		else if (b == menu1_btn) { // 원격 진료
			serviceContentPane.setVisible(false);
			findContentPane.setVisible(true);

		} else if (b == menu2_btn) { // 자가 진단
			serviceContentPane.setVisible(false);
			selfContentPane.setVisible(true);

		} else if (b == menu3_btn) { // 건강 정보
			serviceContentPane.setVisible(false);
			infoContentPane.setVisible(true);

		} else if (b == logout_btn) { // 로그아웃
			try {
				os.close();
				is.close();
				dos.close();
				dis.close();
				socket.close();
			} catch (IOException e1) {
			}
			serviceContentPane.setVisible(false);
			this.setVisible(false);
			Login_UI.setVisible(true);

		}
		// 원격진료 (치과 검색) // Denti_UI2_1
		else if (b == back_1_btn) { // 뒤로 가기 (서비스 화면)
			findContentPane.setVisible(false);
			serviceContentPane.setVisible(true);
		} else if (b == ok1_btn) { // 이름으로 검색
			///////////////////////
			///////////////////////
			findContentPane.setVisible(false);
			listContentPane.setVisible(true);
		} else if (b == ok2_btn) { // 지역으로 검색
			///////////////////////
			///////////////////////
			findContentPane.setVisible(false);
			listContentPane.setVisible(true);
		}
		// 자가진단 // Denti_UI2_2
		else if (b == select1_btn) {
			Denti_UI2_2_1 c1 = new Denti_UI2_2_1();
			c1.setVisible(true);
		} else if (b == select2_btn) {
			Denti_UI2_2_2 c2 = new Denti_UI2_2_2();
			c2.setVisible(true);
		} else if (b == select3_btn) {
			Denti_UI2_2_3 c3 = new Denti_UI2_2_3();
			c3.setVisible(true);
		} else if (b == select4_btn) {
			Denti_UI2_2_4 c4 = new Denti_UI2_2_4();
			c4.setVisible(true);
		} else if (b == select5_btn) {
			Denti_UI2_2_5 c5 = new Denti_UI2_2_5();
			c5.setVisible(true);
		} else if (b == camera_btn) {
			CamCap cam = new CamCap();
			cam.setVisible(true);
		} else if (b == back_btn) { // 뒤로 가기 (서비스 화면)
			selfContentPane.setVisible(false);
			serviceContentPane.setVisible(true);
		}
		// 건강정보 // Denti_UI2_3
		else if (b == select3_1_btn) {
			Denti_UI2_3_1 d1 = new Denti_UI2_3_1();
			d1.setVisible(true);
		} else if (b == select3_2_btn) {
			Denti_UI2_3_2 d2 = new Denti_UI2_3_2();
			d2.setVisible(true);
		} else if (b == select3_3_btn) {
			Denti_UI2_3_3 d3 = new Denti_UI2_3_3();
			d3.setVisible(true);
		} else if (b == select3_4_btn) {
			Denti_UI2_3_4 d4 = new Denti_UI2_3_4();
			d4.setVisible(true);
		} else if (b == select3_5_btn) {
			Denti_UI2_3_5 d5 = new Denti_UI2_3_5();
			d5.setVisible(true);
		} else if (b == select3_6_btn) {
			Denti_UI2_3_6 d6 = new Denti_UI2_3_6();
			d6.setVisible(true);
		} else if (b == back_2_btn) { // 뒤로 가기 (서비스 화면)
			infoContentPane.setVisible(false);
			serviceContentPane.setVisible(true);
		}
		// 회원가입 구분 // Denti_UI1
		else if (b == select_clients_btn) {
			joinMemCheck_ui.setVisible(false);
			joinMemClient_ui.setVisible(true);

		} else if (b == select_clinics_btn) {
			joinMemCheck_ui.setVisible(false);
			joinMemClinic_ui.setVisible(true);

		}
		// 회원가입 (개인) // Denti_UI1_0
		else if (b == join_clients_btn) { // 개인 회원가입 버튼
			// 회원 확인 필요
			///////////////////////

			joinMemClient_ui.setVisible(false);
		}
		// 회원가입 (치과) // Denti_UI1_1
		else if (b == join_clinics_btn) { // 치과 회원가입 버튼
			// 회원 확인 필요
			///////////////////////

			joinMemClinic_ui.setVisible(false);

		}
		// 치과 리스트 창 // ChatRoomList
		else if (b == care_btn) { // 진료받기 버튼 (채팅 입장)
			String JoinRoom = (String) dentistList.getSelectedValue();
			send_message("JoinRoom|" + JoinRoom);

			if (JoinRoom != null) {
				listContentPane.setVisible(false);
				ChattingContentPane.setVisible(true);
			}

		} else if (b == back_3_btn) { // 뒤로 가기 (치과 검색 화면)
			listContentPane.setVisible(false);
			findContentPane.setVisible(true);
		}
		// 채팅 창 // ChattingFrame
		else if (b == sendMsg_btn) { // 메시지 전송 버튼
			String m = write_tf.getText().trim();

			if (m.length() != 0) {
				send_message("Chatting|" + My_Room + "| " + m);
				write_tf.setText("");
				write_tf.requestFocus();
			}

		} else if (b == sendPhoto_btn) { // 사진 전송 버튼
			JFileChooser fc = new JFileChooser();
			File file = new File("capture");
			if (!file.isDirectory()) {
				file.mkdirs();
			}
			fc.setCurrentDirectory(file);

			int ret = fc.showOpenDialog(null);

			if (ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일 보내기를 취소하였습니다.", "경고", JOptionPane.WARNING_MESSAGE);
				return;
			}

			filePath = fc.getSelectedFile().getPath();

			msg_ta.append("*** 파일 전송을 시도합니다. ***\n");
			send_message("FileSendTry|" + My_Room + "|" + filePath);

		} else if (b == quitChat_btn) { // 상담 끝내기 버튼 (치과 리스트 화면)

			send_message("Room_out|" + My_Room + "| ");

			ChattingContentPane.setVisible(false);
			listContentPane.setVisible(true);

		}
		// 채팅 창 (치과용) // ChattingFrame_for_Dentist
		else if (b == sendMsg_btn_D) { // 메시지 전송 버튼
			String m = write_tf_D.getText().trim();

			if (m.length() != 0) {
				send_message("Chatting|" + My_Room + "| " + m);
				write_tf_D.setText("");
				write_tf_D.requestFocus();
			}

		} else if (b == sendPhoto_btn_D) { // 사진 전송 버튼
			JFileChooser fc = new JFileChooser();
			File file = new File("capture");
			if (!file.isDirectory()) {
				file.mkdirs();
			}
			fc.setCurrentDirectory(file);

			int ret = fc.showOpenDialog(null);

			if (ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일 보내기를 취소하였습니다.", "경고", JOptionPane.WARNING_MESSAGE);
				return;
			}

			filePath = fc.getSelectedFile().getPath();

			msg_ta_D.append("** 파일 전송을 시도합니다. **\n");
			send_message("FileSendTry|" + My_Room + "|" + filePath);

		} else if (b == startChat_btn_D) { // 상담 시작 버튼 (방 만들기)
			String roomName = JOptionPane.showInputDialog("방 이름");

			if (roomName != null) {
				send_message("CreateRoom|" + roomName);
			}

		} else if (b == quitChat_btn_D) { // 상담 끝내기 버튼
			///////////////////////
			send_message("Room_out|" + My_Room + "| ");
		}

	}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10) { // Enter 키가 눌려졌을 경우, 전송 버튼을 누르는 것과 동일한 동작
			String m = write_tf.getText().trim();

			if (m.length() != 0) {
				send_message("Chatting|" + My_Room + "| " + m);
				write_tf.setText("");
				write_tf.requestFocus();
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JLabel l = (JLabel) e.getSource();

		if (l == join_lb) { // 회원가입 버튼
			joinMemCheck_ui.setVisible(true);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
