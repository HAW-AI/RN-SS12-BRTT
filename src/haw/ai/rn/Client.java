package haw.ai.rn;

import static haw.ai.rn.Utils.*;

import java.net.*;
import java.io.*;

public class Client {
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
        return readLine(clientSocket.getInputStream());
    }
    
    public static void main(String[] args) throws IOException {
        Client c = new Client(InetAddress.getByName("localhost"), 1337);
        c.start();
        System.out.println("client running");
        
        System.out.println(c.toUpper("hello world"));
        c.stop();
    }
}
