package client;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Denti_UI2_2_7 extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Denti_UI2_2_7 frame = new Denti_UI2_2_7();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Denti_UI2_2_7() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		
		String IconPath = ".\\images\\icons\\simbol.PNG";
		ImageIcon SimbolIcon = new ImageIcon(IconPath);  		
		setIconImage(SimbolIcon.getImage());
		setTitle("Camera");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\uC815\uD55C\uC544 \uCE74\uBA54\uB77C \uB123\uC5B4\uC918!");
		lblNewLabel.setBounds(132, 65, 196, 15);
		contentPane.add(lblNewLabel);
	}

}
