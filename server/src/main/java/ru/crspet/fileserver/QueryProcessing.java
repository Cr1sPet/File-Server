package ru.crspet.fileserver;

import ru.crspet.fileserver.service.DeleteService;
import ru.crspet.fileserver.service.GetService;
import ru.crspet.fileserver.service.PutService;
import ru.crspet.fileserver.utils.SerializationUtils;
import ru.crspet.fileserver.utils.Utils;

import java.io.*;
import java.util.Map;
import java.util.UUID;

public class QueryProcessing  {


    private PutService service;
    private GetService getService;
    private DeleteService deleteService;
    private Map<Integer, String> savedFiles;

    public QueryProcessing(Map<Integer, String> savedFiles) {
        this.savedFiles = savedFiles;

        service = new PutService(savedFiles);
        getService = new GetService(savedFiles);
        deleteService = new DeleteService(savedFiles);
    }

//    private static final String FILES_PATH = System.getProperty("user.dir") +
//            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;

    private static final String FILES_PATH = "/Users/jchopped/goinfre/data/file-server/server/";

    public void putProcess(DataInputStream is, DataOutputStream outputStream) throws IOException {
        try {
            String fileName = is.readUTF();
            fileName = fileName.equals("") ? UUID.randomUUID().toString() : fileName;
            int id = fileName.hashCode();
            synchronized (savedFiles) {
                if (savedFiles.containsKey(id)) {
                    outputStream.writeInt(403);
                    return;
                }
            }
            outputStream.writeInt(200);
            service.saveFile(id, fileName, is);
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