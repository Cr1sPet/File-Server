package ru.crspet.fileserver.service;

import ru.crspet.fileserver.utils.SerializationUtils;
import ru.crspet.fileserver.utils.Utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DeleteService {
    int id;
    private Map<Integer, String> savedFiles;
    public DeleteService(Map<Integer, String> savedFiles) {
        savedFiles = savedFiles;
    }

    public int deleteFile(DataInputStream is) throws IOException {
        String findMethod = is.readUTF();
        id = Utils.getIdFromClient(findMethod, is);
        String filePath = savedFiles.get(id);
        savedFiles.remove(id);
        SerializationUtils.serialize(savedFiles, SerializationUtils.SERIALIZED_FILES_PATH);
        if (! new File(filePath).delete()) {
            return 404;
        }
        return 200;
    }
}
