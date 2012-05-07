package haw.ai.rn;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
    private static final String DELIMITER = "\n";
	private Socket clientSocket;
    private InetAddress ip;
    private int port;
    
    public Client(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public void start() throws IOException {
        if (clientSocket == null) {
            clientSocket = new Socket(ip, port);
        }
    }
    
    public void stop() throws IOException {
        clientSocket.close();
        clientSocket = null;
    }
    
    public boolean isRunning() {
        return clientSocket != null;
    }
    
    public String toUpper(String str) throws IOException {
        if (!isRunning()) {
            throw new RuntimeException("Client not running");
        }
        
        clientSocket.getOutputStream().write((str+"\n").getBytes());
        return new Scanner(clientSocket.getInputStream()).useDelimiter(DELIMITER).next();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        
        for (int i = 0; i < 10; i++) {
        	new Thread(new Runnable() {
				@Override
				public void run() {
					Client client;
					try {
						client = new Client(InetAddress.getByName("localhost"), 1337);
			        	client.start();
			        	System.out.println(client.toUpper("hello world"));
			        	Thread.sleep(300);
			        	System.out.println(client.toUpper("hello world"));
			        	client.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
        }
    }
}
