package server;

import serialization.SerializationUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class QueryProcessing  {

    private static final String FILES_PATH = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;

    public static void writeBytesToFIle(byte []bytes,  File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
    }

    public static byte[] getByteArrayFromFile(String fileName) throws IOException {

        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + fileName + "doesn't extsts!");
        }

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File too large!");
        }

        byte [] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;

        InputStream is = new FileInputStream(file);
        try {
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }
        return bytes;
    }

    public static int getKey(Map<Integer, String> MyFiles) {
        if (MyFiles.isEmpty()) {
            return 1;
        } else {
            Iterator<Integer> it = MyFiles.keySet().iterator();
            int last = 0;
            while (it.hasNext()) {
                last = it.next();
            }
            return last + 1;
        }
    }

    public static Integer getI(Map<Integer,String> savedFiles) {
        long max = Long.MAX_VALUE;
        long min = Long.MIN_VALUE;

        Integer i = (int) Math.floor(Math.random() * (max - min + 1) + min);

        while (!savedFiles.containsKey(i)) {
            i = (int) Math.floor(Math.random() * (max - min + 1)+min);
        }
        return i;
    }


    public static void putProcess(Map<Integer, String> savedFiles, DataInputStream is, DataOutputStream outputStream) {
        try {
            int fileId;
            String fileName = is.readUTF();
            if (fileName.equals(null) || fileName.equals("")) {
                fileId = getI(savedFiles);
                fileName = "unnamed" + fileId;
            } else {
                fileId = fileName.hashCode();
            }
            File file = new File(FILES_PATH  + fileName);
            if (file.exists()) {
                System.out.println("File exist");
                System.out.println("fileName to save : " + file.getAbsolutePath());
                outputStream.writeInt(403);
                return;
            } else {
                outputStream.writeInt(200);
            }
            int bytesSize = is.readInt();
            byte []bytes = new byte[bytesSize];
            is.readFully(bytes, 0, bytes.length);
            writeBytesToFIle(bytes, file);
            synchronized (savedFiles) {
                savedFiles.put(fileId, file.getPath());
                SerializationUtils.serialize(savedFiles, SerializationUtils.SERIALIZED_FILES_PATH + "savedFilesInfo");
                outputStream.writeUTF("200 " + fileId);
            }
        } catch (IOException e) {
            System.out.println("IOException in PutProcess");
            System.out.println(e);
            e.printStackTrace();
            try {
                outputStream.writeUTF("500");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String GetProcess(DataInputStream is, DataOutputStream os, Map<Integer, String> MyFiles) throws IOException {
        byte [] bytes;
        try {
            if  (is.readUTF().equals("name")) {
                String fileName = FILES_PATH + is.readUTF();
                try {
                    bytes = getByteArrayFromFile(fileName);
                    os.writeInt(200);
                    System.out.println("BYTES SIZE ON SERVER + " + bytes.length);
                    os.writeInt(bytes.length);
                    os.write(bytes);
                } catch (FileNotFoundException e) {
                    System.out.println(e);
                    os.writeInt(403);
                    return "200";
                }
            } else {
                int id = is.readInt();
                if (MyFiles.containsKey(id)) {
                    String fileName = MyFiles.get(id);
                    bytes = getByteArrayFromFile(fileName);
                    System.out.println("BYTES SIZE ON SERVER + " + bytes.length);
                    os.writeInt(200);
                    System.out.println(bytes.length);
                    os.writeInt(bytes.length);
                    os.write(bytes);
                } else {
                    System.out.println("File don't exist");
                    os.writeInt(403);
                    return "200";
                }
            }
        } catch (IOException e) {
            System.out.println("IOException in PutProcess");
            e.printStackTrace();
            os.writeInt(500);
            return "500";
        }

        return  "200 ";
    }

    public static String DeleteProcess(DataInputStream is, DataOutputStream os, Map<Integer, String> MyFile) throws IOException {
            String fileName = "";
            String filePath = "";
            if (is.readUTF().equals("name")) {
                filePath = FILES_PATH + is.readUTF();
            } else {
                int id = is.readInt();
                filePath = MyFile.get(id);
                MyFile.remove(id);
                SerializationUtils.serialize(MyFile, SerializationUtils.SERIALIZED_FILES_PATH);
            }
            try {
                if (!Files.deleteIfExists(Path.of(filePath))) {
                    os.writeUTF("404");
                }
            } catch (IOException e) {
                os.writeUTF("500");
            }
            os.writeUTF("200");
            return "200";
    }
}
