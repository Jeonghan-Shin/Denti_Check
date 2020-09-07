package client;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSender extends Thread {

	private int port;
	private String filePath;

	private ServerSocket server;
	private Socket socket;
	private FileInputStream fis;
//	private BufferedInputStream bis;
	private OutputStream out;
//	private BufferedOutputStream bos;
	private byte[] buffer = new byte[1024];

	public FileSender() {

	}

	public FileSender(int port, String filePath) {
		this.port = port;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(7000);

			socket = server.accept();

			// 파일전송 시작
			fis = new FileInputStream(filePath);
//			bis = new BufferedInputStream(fis);
			

			out = socket.getOutputStream();
//			bos = new BufferedOutputStream(out);

			int r = 0;

			while ((r = fis.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, r);
			}

			// 파일전송 완료

		} catch (Exception e) {
			System.out.println("파일전송 실패");
		} finally {
			try {
//				bos.close();
//				bis.close();
				out.close();
				fis.close();
				socket.close();
				server.close();
			} catch (IOException e) {
			}

		}
	}
}
