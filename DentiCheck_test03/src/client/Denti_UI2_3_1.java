package client;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class Denti_UI2_3_1 extends JFrame {

	private JPanel contentPane;

	public Denti_UI2_3_1() {
		setLocationRelativeTo(null);
		setBounds(100, 100, 758, 650);
		setLocationRelativeTo(null);
		setTitle("치아 건강정보- 올바른 양치법");
		String IconPath = ".\\images\\icons\\simbol.PNG";
		ImageIcon SimbolIcon = new ImageIcon(IconPath);  		
		setIconImage(SimbolIcon.getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 748, 708);
		scrollPane.getVerticalScrollBar().setUnitIncrement(23);
		contentPane.add(scrollPane);
		
		String imgPath = ".\\images\\2_3\\2_3_1.JPG";
		ImageIcon originIcon = new ImageIcon(imgPath);  

		Image originImg = originIcon.getImage(); 
		Image changedImg= originImg.getScaledInstance(738, 602, Image.SCALE_SMOOTH );
		ImageIcon Icon = new ImageIcon(changedImg);

		
		JLabel Label1 = new JLabel("");
		Label1.setIcon(new ImageIcon(changedImg));
		scrollPane.setColumnHeaderView(Label1);
	}

}
