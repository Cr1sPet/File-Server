package server;

import java.io.IOException;
import java.net.ServerSocket;
import server.Session;

public class PortListening extends Thread{
    int PORT;
    PortListening(final int PORT) {
        this.PORT = PORT;
    }
    @Override
    public void run() {
        while (true) {
            try (ServerSocket server = new ServerSocket(PORT)) {
                Session session = new Session(server.accept());
                System.out.println("client accepted");
                session.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
