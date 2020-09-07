package client;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public class Denti_UI2_3_2 extends JFrame {

	private JPanel contentPane;

	public Denti_UI2_3_2() {
		setBounds(100, 100, 757, 695);		//사이즈 고정!
		setLocationRelativeTo(null);
		setTitle("치아 건강정보- 나이대별 치아 관리");
		String IconPath = ".\\images\\icons\\simbol.PNG";
		ImageIcon SimbolIcon = new ImageIcon(IconPath);  		
		setIconImage(SimbolIcon.getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(23);
		scrollPane.setBounds(0, 0, 740, 650);
		contentPane.add(scrollPane);
		
		String imgPath = ".\\images\\2_3\\2_3_2_0.JPG";
		ImageIcon originIcon = new ImageIcon(imgPath);  

		Image originImg = originIcon.getImage(); 
		Image changedImg = originImg.getScaledInstance(740, 1800, Image.SCALE_SMOOTH );
		ImageIcon Icon = new ImageIcon(changedImg);
		
		JLabel image = new JLabel("");
		image.setIcon(new ImageIcon(changedImg));

		scrollPane.setViewportView(image);
	}
}

