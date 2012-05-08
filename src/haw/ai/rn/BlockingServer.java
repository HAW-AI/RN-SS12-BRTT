package haw.ai.rn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class BlockingServer {
	public static String DELIMITER = "\n";
	private ServerSocket socket;
	private Integer requestCounter = 0;
	private HashMap<InetAddress, Integer> requestCounterByInetAddress = new HashMap<InetAddress, Integer>();
	private HashMap<InetAddress, Long> connectionTimeByInetAddress = new HashMap<InetAddress, Long>();
	
	public BlockingServer(Integer port) throws IOException {
		socket = new ServerSocket(port);
	}
	
	public void run() throws IOException {
		Socket current;
		boolean connected;
		while((current = socket.accept()) != null) {
			connected = true;
			while (connected) {
				try {
					String msg = new Scanner(current.getInputStream()).useDelimiter(DELIMITER).next();
					if (msg.trim().equals("end")) {
						current.getOutputStream().write(("abort"+DELIMITER).getBytes());
						System.out.println(String.format("%s requests end of connection", current.getInetAddress()));
						current.close();
						connected = false;
					}
					else {
						current.getOutputStream().write((msg+DELIMITER).toUpperCase().getBytes());
					}
					log(current);
				}
				catch (Exception e) {
					connected = false;
				}
			}
			removeConnection(current);
		}
	}
	
    private void removeConnection(Socket socket) {
		requestCounterByInetAddress.remove(socket.getInetAddress());
		connectionTimeByInetAddress.remove(socket.getInetAddress());
	}

	private void log(Socket socket) {
    	String now = (new Date()).toString();
    	InetAddress ip = socket.getInetAddress();
    	int count = incRequestCounter();
    	int clientCount = incRequestCounterByInetAddress(ip);
    	Long clientTime = getConnectionTimeByInetAddress(ip); 
    	
    	System.out.println(String.format("[%s] %s Request#: %d ClientRequest#: %d ActiveSince: %s", now, ip, count, clientCount, clientTime));
    }
    
	private Long getConnectionTimeByInetAddress(InetAddress ip) {
		if (!connectionTimeByInetAddress.containsKey(ip)) {
			connectionTimeByInetAddress.put(ip, System.currentTimeMillis());
    	}
    	return System.currentTimeMillis() - connectionTimeByInetAddress.get(ip);
	}
	
	private int incRequestCounterByInetAddress(InetAddress ip) {
    	if (!requestCounterByInetAddress.containsKey(ip)) {
    		requestCounterByInetAddress.put(ip, 0);
    	}
    	requestCounterByInetAddress.put(ip, requestCounterByInetAddress.get(ip)+1);
    	return requestCounterByInetAddress.get(ip);
	}

	private int incRequestCounter() {
		return ++this.requestCounter;
	}

	public static void main(String[] args) {
		try {
			new BlockingServer(1337).run();
		} catch (IOException e) {
			System.out.println("ups");
			e.printStackTrace();
		}
	}
}
