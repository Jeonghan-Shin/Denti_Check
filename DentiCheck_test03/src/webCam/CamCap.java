package webCam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;

public class CamCap extends JFrame
		implements WebcamListener, WindowListener, UncaughtExceptionHandler, ItemListener, WebcamDiscoveryListener {

	private JPanel contentPane;
	private JPanel panel_cam;
	private JPanel panel_btn;

	private JButton btnStart;
	private JButton btnCapture;

	private Webcam webcam = null;
	private WebcamPanel panel = null;
	private WebcamPicker picker = null;

//public static void main(String[] args) {
//EventQueue.invokeLater(new Runnable() {
//	public void run() {
//		try {
//			CamCap frame = new CamCap();
//			frame.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//});
//}

	public CamCap() {
		String IconPath = ".\\images\\icons\\simbol.PNG";
		ImageIcon SimbolIcon = new ImageIcon(IconPath);
		setIconImage(SimbolIcon.getImage());
		setTitle("Camera");

		Webcam.addDiscoveryListener(this);

		addWindowListener(this);

		picker = new WebcamPicker();
		picker.addItemListener(this);

		webcam = picker.getSelectedWebcam();

		if (webcam == null) {
//    System.out.println("No webcams found...");
			JOptionPane.showMessageDialog(null, "카메라를 찾을 수 없습니다.", "Error", ERROR);
			System.exit(1);
		}

		webcam.setViewSize(WebcamResolution.VGA.getSize());
		webcam.addWebcamListener(CamCap.this);

//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		panel_cam = new JPanel();
		panel_cam.setBackground(Color.DARK_GRAY);

		panel_btn = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(panel_btn, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_cam, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE).addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_btn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 331,
										Short.MAX_VALUE)
								.addComponent(panel_cam, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 219,
										Short.MAX_VALUE))
						.addContainerGap()));
		panel_btn.setLayout(null);
		panel_btn.setBackground(SystemColor.activeCaption);

		btnStart = new JButton("카메라 ON");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCapture.setEnabled(true);
				Thread t = new Thread() {
					@Override
					public void run() {
						panel.start();
					}
				};
				t.setDaemon(true);
				t.start();
			}
		});
		btnStart.setBounds(0, 0, 105, 27);
		panel_btn.add(btnStart);

		btnCapture = new JButton("캡 쳐");
		btnCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File file = new File("capture");
					if (!file.isDirectory()) {
						file.mkdirs();
					}
					file = new File(String.format("capture\\capture-%s.jpg",
							new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(System.currentTimeMillis()))));
					ImageIO.write(webcam.getImage(), "JPG", file);
					JOptionPane.showMessageDialog(null, "사진 캡쳐 : \n" + file.getAbsolutePath(), "CamCap", 1);
				} catch (IOException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage(), "error", ERROR);
				}
			}
		});
		btnCapture.setBounds(0, 30, 105, 27);
		panel_btn.add(btnCapture);
		btnCapture.setEnabled(false);

		picker.setBounds(0, 60, 105, 27);
		panel_btn.add(picker);

		panel_cam.setLayout(new BorderLayout(0, 0));
		contentPane.setLayout(gl_contentPane);

		panel = new WebcamPanel(webcam, false);
		panel.setMirrored(true);
		panel.setFillArea(true);
		panel_cam.add(panel, BorderLayout.CENTER);

	}

	@Override
	public void webcamFound(WebcamDiscoveryEvent event) {
		if (picker != null) {
			picker.addItem(event.getWebcam());
		}
	}

	@Override
	public void webcamGone(WebcamDiscoveryEvent event) {
		if (picker != null) {
			picker.removeItem(event.getWebcam());
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() != webcam) {
			if (webcam != null) {

				panel.stop();

				remove(panel);

				webcam.removeWebcamListener(this);
				webcam.close();

				webcam = (Webcam) e.getItem();
				webcam.setViewSize(WebcamResolution.VGA.getSize());
				webcam.addWebcamListener(this);

				System.out.println("selected " + webcam.getName());

				panel = new WebcamPanel(webcam, false);

				// 새로운 카메라 panel 채우기
				panel.setMirrored(true);
				panel.setFillArea(true);
				panel_cam.removeAll();
				panel_cam.setLayout(new BorderLayout(0, 0));
				panel_cam.add(panel, BorderLayout.CENTER);
				validate();
				setVisible(true);
				pack();

				Thread t = new Thread() {

					@Override
					public void run() {
						panel.start();
					}
				};
				t.setName("example-stoper");
				t.setDaemon(true);
				t.setUncaughtExceptionHandler(this);
				t.start();
			}
		}
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.err.println(String.format("Exception in thread %s", t.getName()));
		e.printStackTrace();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		webcam.close();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		System.out.println("webcam viewer resumed");
		panel.resume();
	}

	@Override
	public void windowIconified(WindowEvent e) {
		System.out.println("webcam viewer paused");
		panel.pause();
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void webcamClosed(WebcamEvent arg0) {
		System.out.println("webcam closed");
	}

	@Override
	public void webcamDisposed(WebcamEvent arg0) {
		System.out.println("webcam disposed");
	}

	@Override
	public void webcamImageObtained(WebcamEvent arg0) {
// do nothing
	}

	@Override
	public void webcamOpen(WebcamEvent arg0) {
		System.out.println("webcam open");
	}
}
