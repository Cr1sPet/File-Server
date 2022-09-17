package ru.crspet.fileserver.service;


import ru.crspet.fileserver.entity.UserFile;
import ru.crspet.fileserver.utils.SerializationUtils;


import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import static ru.crspet.fileserver.utils.PutUtils.writeBytesToFile;

public class PutService {

    private static final String FILES_PATH = "/Users/jchopped/goinfre/data/file-server/server/";

    private Map<Integer, String> savedFiles;

    public PutService() {}
    public PutService(Map<Integer, String> savedFiles) {
        this.savedFiles = savedFiles;
    }

    public UserFile fillUserFile(String fileName) {
        if (fileName == "") {
            fileName = UUID.randomUUID().toString();
        }
        String filePath = FILES_PATH + fileName;
        synchronized (savedFiles) {

            if (savedFiles.containsKey(filePath.hashCode())) {
                return null;
            }
        }
         return new UserFile(filePath.hashCode(),
                             fileName,
                             filePath);
    }

    public void saveFile(int id, String fileName, DataInputStream is) throws IOException {
        int bytesSize = is.readInt();
        byte []bytes = new byte[bytesSize];
        is.readFully(bytes, 0, bytes.length);

        String filePath = FILES_PATH + fileName;

        writeBytesToFile(bytes, filePath);
        synchronized (savedFiles) {
            savedFiles.put(id, filePath);
            SerializationUtils.serialize(savedFiles, SerializationUtils.SERIALIZED_FILES_PATH + "savedFilesInfo");
        }
    }
}
