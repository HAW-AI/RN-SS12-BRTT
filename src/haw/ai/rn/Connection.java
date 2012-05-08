package haw.ai.rn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Connection implements Runnable {
	private Socket socket = null;
	private String delimiter;
	private int requestCounter = 0;
	private long connectionTime = System.currentTimeMillis();
	
	public Connection(Socket socket, String delimiter) {
		this.setSocket(socket);
		this.delimiter = delimiter;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		boolean connected = true;
		while (connected) {
			try {
				InputStream is = getSocket().getInputStream();
				OutputStream os = getSocket().getOutputStream();
				
				boolean e,n,d;
				e = n = d = false;
				
				int c;
				while((c = is.read()) != -1) {
					if (c == 'e') e = true;
					if (c == 'n') n = true;
					if (c == 'd') d = true;
					if (e && n && d && c == 10) {
						os.write(("abort"+delimiter).getBytes());
						System.out.println(String.format("%s requests end of connection", getSocket().getInetAddress()));
						connected = false;
						break;
					}
					if (c != 'e' && c != 'n' && c != 'd' && c != 10) {
						e = n = d = false;
					}
					os.write(Character.toUpperCase(c));
				}
				log();
			}
			catch (NoSuchElementException e) {
				connected = false;
			}
			catch (Exception e) {
				connected = false;
				e.printStackTrace();
			}
		}
		try {
			System.out.println("close connection to " + socket.getInetAddress());
			socket.close();
			socket = null;
		} catch (IOException e) {
			System.out.println(String.format("Can't close connection to %s", socket.getInetAddress()));
			e.printStackTrace();
		}
	}

	private void log() {
    	String now = (new Date()).toString();
    	InetAddress ip = getSocket().getInetAddress();
    	
    	System.out.println(String.format("[%s] %s ClientRequest#: %d ActiveSince: %s", now, ip, incRequestCounter(), getConnectionTime()));
    }
	
	private int incRequestCounter() {
		return ++requestCounter ;
	}

	private Long getConnectionTime() {
		
		return System.currentTimeMillis() - connectionTime ;
	}

	public Socket getSocket() {
		return socket;
	}
}
