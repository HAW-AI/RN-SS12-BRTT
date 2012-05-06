package haw.ai.rn;

import java.io.IOException;
import java.net.InetAddress;

public final class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        final InetAddress ip = InetAddress.getByName("localhost");
        final int port = 1337;
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                try {
                    new IterativeServer(port);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        
        System.out.println("server running");
        
        Thread.sleep(2000);
        
        Client client = new Client(ip, port);
        client.start();
        System.out.println("client running");
        
        System.out.println(client.toUpper("hello world"));
        client.stop();
        
    }

}
