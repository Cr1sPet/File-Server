package server;

import serialization.SerializationUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedHashMap;
import java.util.Map;

public class PortListening extends Thread{
    int PORT;
    PortListening(final int PORT) {
        this.PORT = PORT;
    }

    public Map<Integer, String> getMyFiles() {
        Map<Integer, String> MyFiles;
        try {
            MyFiles = (Map<Integer, String>) SerializationUtils.deserialize(SerializationUtils.SERIALIZED_FILES_PATH);
        } catch (IOException | ClassNotFoundException e) {
            MyFiles = new LinkedHashMap<>();
            System.out.println("Exception OK");
        }
        return MyFiles;
    }

    @Override
    public void run() {
        Map<Integer, String> MyFiles = getMyFiles();
        while (true) {
            try (ServerSocket server = new ServerSocket(PORT)) {
                Session session = new Session(server.accept(), MyFiles);
                System.out.println("client accepted");
                session.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
