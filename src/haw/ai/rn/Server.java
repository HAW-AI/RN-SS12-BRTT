package haw.ai.rn;

import static haw.ai.rn.Utils.*;

import java.io.IOException;
import java.net.Socket;

public abstract class Server {
    protected void serveClient(Socket connectionSocket) throws IOException {
        String input = readLine(connectionSocket.getInputStream());
        String output = input.toUpperCase();
        connectionSocket.getOutputStream().write((output+"\n").getBytes());
    }
}
