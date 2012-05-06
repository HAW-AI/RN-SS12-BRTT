package haw.ai.rn;

import java.net.*;
import java.io.*;

public class IterativeServer extends Server {
    public IterativeServer(int port) throws IOException {
        ServerSocket welcomeSocket = new ServerSocket(port);
        
        System.out.println("waiting for requests");
        
        while (true) {
            serveClient(welcomeSocket.accept());
        }
    }
    
    public static void main(String[] args) throws IOException {
        Server s = new IterativeServer(1337);
    }
}
