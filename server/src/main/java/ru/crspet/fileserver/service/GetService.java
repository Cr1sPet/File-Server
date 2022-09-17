package ru.crspet.fileserver.service;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

import static ru.crspet.fileserver.utils.GetUtils.getByteArrayFromFile;

public class GetService {

    Map<Integer, String> savedFiles;

    public GetService(Map<Integer, String> savedFiles) {
        this.savedFiles = savedFiles;
    }

    public byte[] getFileBytes(String findMethod, DataInputStream is) throws IOException {
        int id;
        byte[] bytes;
        if (findMethod.equals("name")) {
            id = is.readUTF().hashCode();
        } else {
            id = is.readInt();
        }
        if (savedFiles.containsKey(id)) {
            String filePath = savedFiles.get(id);
            bytes = getByteArrayFromFile(filePath);
            return bytes;
        }
        return null;
    }
}
