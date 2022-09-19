package ru.crspet.fileserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.crspet.fileserver.repositories.UserFileRepo;
import ru.crspet.fileserver.service.DeleteService;
import ru.crspet.fileserver.service.GetService;
import ru.crspet.fileserver.service.PutService;
import ru.crspet.fileserver.utils.SerializationUtils;
import ru.crspet.fileserver.utils.Utils;

import java.io.*;
import java.util.Map;
import java.util.UUID;


@Component
public class QueryProcessing  {

    @Autowired
    @Qualifier("userFileRepoImpl")
    private UserFileRepo userFileRepo;

    @Autowired
    private PutService putService;
    @Autowired

    private GetService getService;
    @Autowired

    private DeleteService deleteService;

    public QueryProcessing() {}


    public void putProcess(DataInputStream is, DataOutputStream outputStream) throws IOException {
        try {
            String fileName = putService.generateFileName(is.readUTF());
            int id = fileName.hashCode();
            if (userFileRepo.findById(id) != null) {
                outputStream.writeInt(403);
                return;
            }
            outputStream.writeInt(200);
            putService.saveFile(fileName, is);
            outputStream.writeUTF("200 " + id);
        } catch (IOException e) {
            outputStream.writeUTF("500");
        }
    }

    public  void getProcess(DataInputStream is, DataOutputStream os) throws IOException {
        try {
            String findMethod = is.readUTF();
            byte [] bytes = getService.getFileBytes(findMethod, is);
            if (bytes != null) {
                os.writeInt(200);
                os.writeInt(bytes.length);
                os.write(bytes);
            } else {
                System.out.println("File don't exist");
                os.writeInt(403);
            }
        } catch (IOException e) {
            System.out.println("IOException in PutProcess");
            e.printStackTrace();
            os.writeInt(500);
        }
    }

    public void deleteProcess(DataInputStream is, DataOutputStream os) throws IOException {
        int returnCode;
        try {
            returnCode = deleteService.deleteFile(is);
            os.writeInt(returnCode);
        } catch (IOException e) {
            e.printStackTrace();
            os.writeInt(500);
        }
    }
}