package ru.crspet.fileserver;

import ru.crspet.fileserver.utils.SerializationUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class PortListening {
    int PORT;

    public PortListening(final int PORT) {
        this.PORT = PORT;
    }

    public Map<Integer, String> getSavedFiles() {
        Map<Integer, String> savedFilesMap;
        try {
            savedFilesMap = (Map<Integer, String>) SerializationUtils.deserialize(SerializationUtils.SERIALIZED_FILES_PATH);
        } catch (IOException | ClassNotFoundException e) {
            savedFilesMap = new HashMap<>();
        }
        if (savedFilesMap == null) {
            savedFilesMap = new HashMap<>();
        }
        return savedFilesMap;
    }

    public void listen() {
        Map<Integer, String> savedFilesMap = getSavedFiles();
        while (true) {
            try (ServerSocket server = new ServerSocket(PORT)) {
                Session session = new Session(server.accept(), savedFilesMap);
                System.out.println("client accepted");
                session.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
