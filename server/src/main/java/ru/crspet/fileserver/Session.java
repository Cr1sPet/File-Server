package ru.crspet.fileserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Session extends Thread {

    private Socket socket;
    private QueryProcessing queryProcessing;

    Session(Socket socket, Map<Integer, String> savedFiles) {
        this.socket = socket;
        queryProcessing = new QueryProcessing(savedFiles);
    }

    @Override
    public void run() {
        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ){
            while (true) {
                String requestString = inputStream.readUTF();

                Request request = Request.valueOf(requestString);
                switch (request) {
                    case PUT:
                        queryProcessing.putProcess(inputStream, outputStream);
                        break;
                    case GET:
                        queryProcessing.getProcess(inputStream, outputStream);
                        break;
                    case DELETE:
                        queryProcessing.deleteProcess(inputStream, outputStream);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client was disconnected! " + e.getMessage());
            return;
        }
    }
}