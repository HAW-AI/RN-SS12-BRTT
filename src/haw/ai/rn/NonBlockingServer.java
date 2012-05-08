package haw.ai.rn;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class NonBlockingServer {
	public static String DELIMITER = "\n";
	private ServerSocket socket;
	private ThreadPoolExecutor threads = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
	
	public NonBlockingServer(Integer port) throws IOException {
		socket = new ServerSocket(port);
	}
	
	public void run() throws IOException {
		while (true) {
			if (threads.getPoolSize() > threads.getActiveCount())
				threads.execute(new Connection(socket.accept(), DELIMITER));
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
