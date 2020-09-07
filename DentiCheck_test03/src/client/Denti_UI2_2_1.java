package client;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class Denti_UI2_2_1 extends JFrame {

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Denti_UI2_2_1 frame = new Denti_UI2_2_1();
//					frame.setVisible(true);
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public Denti_UI2_2_1() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 640);
		setLocationRelativeTo(null);
		setTitle("자가진단 - 충치");
		String IconPath = ".\\images\\icons\\simbol.PNG";
		ImageIcon SimbolIcon = new ImageIcon(IconPath);  		
		setIconImage(SimbolIcon.getImage());
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 738, 602);
		getContentPane().add(scrollPane);
		
		String imgPath = ".\\images\\2_2\\2_2_1.JPG";
		ImageIcon originIcon = new ImageIcon(imgPath);  

		Image originImg = originIcon.getImage(); 
		Image changedImg= originImg.getScaledInstance(738, 602, Image.SCALE_SMOOTH );
		ImageIcon Icon = new ImageIcon(changedImg);

		
		JLabel Label1 = new JLabel("");
		Label1.setIcon(new ImageIcon(changedImg));
		scrollPane.setColumnHeaderView(Label1);
	}
}
