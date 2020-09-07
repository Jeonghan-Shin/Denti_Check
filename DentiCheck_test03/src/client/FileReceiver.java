package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileReceiver extends Thread {

	private String filename;

	private Socket socket;
	private InputStream in;
	private FileOutputStream fos;
//	private BufferedInputStream bis;
//	private BufferedOutputStream bos;
	private byte[] buffer = new byte[1024];

	public FileReceiver(String ip, int port, String filename) {
		try {
			this.filename = filename;
			socket = new Socket(ip, port);

			// 파일 다운로드 시작
			in = socket.getInputStream();
//			bis = new BufferedInputStream(in);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String fileSeparator = System.getProperty("file.separator");

			File f = new File("Down");
			if (!f.isDirectory()) {
				f.mkdir();
			}

//			in.read(buffer, 0, 10);
//			String header = new String(buffer, 0, 10);
//			int bodySize = Integer.parseInt(header);
//			int readSize = 0;
			
			fos = new FileOutputStream("Down" + fileSeparator + filename);
//			bos = new BufferedOutputStream(fos);

			int r = 0;
			while ((r = in.read(buffer, 0, buffer.length)) > 0) {
				fos.write(buffer, 0, r);
			}
			
			// 파일다운 완료

		} catch (FileNotFoundException e) {
			System.out.println("예외: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("파일전송 실패");
		} finally {
			try {

				fos.close();
				in.close();
//				bis.close();
//				bos.close();
				socket.close();
			} catch (IOException e) {
			}
		}
	}

}
