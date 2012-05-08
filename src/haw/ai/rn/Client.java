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
    
    private void send(String msg) throws IOException {
    	clientSocket.getOutputStream().write((msg+"\n").getBytes());
    }
    
    public void stop() throws IOException {
        send("end");
        if (new Scanner(clientSocket.getInputStream()).useDelimiter(DELIMITER).next().trim().equals("abort")) {
        	clientSocket.close();
        	clientSocket = null;
        }
    }
    
    public boolean isRunning() {
        return clientSocket != null;
    }
    
    public String toUpper(String str) throws IOException {
        if (!isRunning()) {
            throw new RuntimeException("Client not running");
        }
        
        send(str);
        return new Scanner(clientSocket.getInputStream()).useDelimiter(DELIMITER).next();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        
        for (int i = 0; i < 11; i++) {
        	new Thread(new Runnable() {
				@Override
				public void run() {
					Client client;
					try {
						client = new Client(InetAddress.getByName("lab30"), 1337);
			        	client.start();
			        	System.out.println(client.toUpper("hello world"));
			        	Thread.sleep((long) (Math.random() * 10000));
			        	System.out.println(client.toUpper("bye world"));
			        	client.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
        }
    }
}
