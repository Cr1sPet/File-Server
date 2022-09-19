package ru.crspet.fileserver.utils;

import java.io.*;

public class SerializationUtils {


//    public static final String SERIALIZED_FILES_PATH = System.getProperty("user.dir") +
//            File.separator + "src" + File.separator + "serialization" + File.separator;

    public static final String SERIALIZED_FILES_PATH = "/Users/jchopped/MyFileServer/server/src/main/java/ru/crspet/fileserver/utils/saved";

    public static void serialize(Object obj, String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
        fos.close();
    }

    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        fis.close();
        return obj;
    }

}
