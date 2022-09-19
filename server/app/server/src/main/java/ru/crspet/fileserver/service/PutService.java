package ru.crspet.fileserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.crspet.fileserver.repositories.UserFileRepo;
import ru.crspet.fileserver.repositories.UserFileRepoImpl;

import javax.annotation.PostConstruct;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import static ru.crspet.fileserver.utils.PutUtils.writeBytesToFile;

@Service
public class PutService {

    private String FILES_PATH;

    @Autowired
    @Qualifier("userFileRepoImpl")
    private UserFileRepo userFileRepo;

    @PostConstruct
    public void construct() {
        FILES_PATH = UserFileRepoImpl.FILES_PATH;
    }


    public PutService() {}


    public void saveFile(String fileName, DataInputStream is) throws IOException {
        int bytesSize = is.readInt();
        byte []bytes = new byte[bytesSize];
        is.readFully(bytes, 0, bytes.length);

        String filePath = FILES_PATH + fileName;

        writeBytesToFile(bytes, filePath);
        userFileRepo.save(fileName, filePath);
    }

    public String generateFileName(String fileName) {
        return fileName.equals("") ? UUID.randomUUID().toString() : fileName;
    }
}
