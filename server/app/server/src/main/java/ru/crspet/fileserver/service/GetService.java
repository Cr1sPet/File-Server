package ru.crspet.fileserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.crspet.fileserver.repositories.UserFileRepo;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

import static ru.crspet.fileserver.utils.GetUtils.getByteArrayFromFile;

@Service
public class GetService {


    @Autowired
    @Qualifier("userFileRepoImpl")
    private UserFileRepo userFileRepo;

    public GetService() {}

    public byte[] getFileBytes(String findMethod, DataInputStream is) throws IOException {
        byte[] bytes;
        String filePath;
        if (findMethod.equals("name")) {
            filePath = userFileRepo.findByFileName(is.readUTF());
        } else {
            filePath = userFileRepo.findById(is.readInt());
        }
        if (filePath != null) {
            bytes = getByteArrayFromFile(filePath);
            return bytes;
        }
        return null;
    }
}
