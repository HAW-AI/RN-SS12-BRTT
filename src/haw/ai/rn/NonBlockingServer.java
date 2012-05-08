package haw.ai.rn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NonBlockingServer {
	public static String DELIMITER = "\n";
	private ServerSocket socket;
	private ExecutorService threads = Executors.newFixedThreadPool(10);
	
	public NonBlockingServer(Integer port) throws IOException {
		socket = new ServerSocket(port);
	}
	
	public void run() throws IOException {
		Socket current;
		
		while((current = socket.accept()) != null) {
			threads.execute(new Connection(current, DELIMITER));
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
