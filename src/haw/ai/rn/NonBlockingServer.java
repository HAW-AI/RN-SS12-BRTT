package haw.ai.rn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NonBlockingServer {
	public static String DELIMITER = "\n";
	private ServerSocket socket;
	
	public NonBlockingServer(Integer port) throws IOException {
		socket = new ServerSocket(port);
	}
	
	public void run() throws IOException {
		Socket current;
		
		while((current = socket.accept()) != null) {
			new Thread(new Connection(current, DELIMITER)).start();
		}
	}
	
	public static void main(String[] args) {
		try {
			new NonBlockingServer(1337).run();
		} catch (IOException e) {
			System.out.println("ups");
			e.printStackTrace();
		}
	}
}
