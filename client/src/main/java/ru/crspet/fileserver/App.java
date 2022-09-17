package ru.crspet.fileserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.sleep(1000);
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ) {

            Activity.routine(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
