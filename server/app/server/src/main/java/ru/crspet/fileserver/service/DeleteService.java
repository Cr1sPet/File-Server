package ru.crspet.fileserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.crspet.fileserver.repositories.UserFileRepo;
import ru.crspet.fileserver.utils.SerializationUtils;
import ru.crspet.fileserver.utils.Utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class DeleteService {
    int id;

    @Autowired
    @Qualifier("userFileRepoImpl")
    private UserFileRepo userFileRepo;

    public DeleteService(Map<Integer, String> savedFiles) {
        savedFiles = savedFiles;
    }

    public int deleteFile(DataInputStream is) throws IOException {
        String findMethod = is.readUTF();
        id = Utils.getIdFromClient(findMethod, is);
        String filePath = userFileRepo.findById(id);
        if (filePath == null) {
            return 404;
        }
        userFileRepo.deleteById(id);
        if (!new File(filePath).delete()) {
            return 404;
        }
        return 200;
    }
}
