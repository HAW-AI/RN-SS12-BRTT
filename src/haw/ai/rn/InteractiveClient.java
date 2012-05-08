package haw.ai.rn;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class InteractiveClient {
    private static final String DELIMITER = "\n";
	private Socket clientSocket;
    private InetAddress ip;
    private int port;
    
    public InteractiveClient(InetAddress ip, int port) {
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
    
    private String read() throws IOException {
    	return new Scanner(clientSocket.getInputStream()).useDelimiter(DELIMITER).next();
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
        
        send(str);
        return read();
    }
    
    public static String readLine() throws IOException {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	System.out.print("$> ");
    	return reader.readLine();
    }
    
    public static void main(String[] args) throws UnknownHostException {
    	InteractiveClient client;
		client = new InteractiveClient(InetAddress.getByName("localhost"), 1337);
    	try {
			client.start();
    	
	    	while (client.isRunning()) {
	    		client.send(readLine());
	    		String msg = client.read();
	    		if (msg.trim().equals("abort")) {
	    			client.stop();
	    			System.out.println("connection closed");
	    		}
	    		else {
	    			System.out.println(msg);
	    		}
	    	}
    	} catch (IOException e) {
			if (client.isRunning()) {
				try {
					client.stop();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
    }
}
