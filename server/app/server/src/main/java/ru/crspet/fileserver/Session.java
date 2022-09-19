package ru.crspet.fileserver;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.crspet.fileserver.config.AppConfig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
public class Session extends Thread {

    private Socket socket;

    private QueryProcessing queryProcessing;

    AnnotationConfigApplicationContext context;
    public Session() {}
    public Session(Socket socket) {
        this.socket = socket;
        context = new AnnotationConfigApplicationContext(AppConfig.class);
        queryProcessing = context.getBean(QueryProcessing.class);
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
            context.close();
            return;
        }
    }
}