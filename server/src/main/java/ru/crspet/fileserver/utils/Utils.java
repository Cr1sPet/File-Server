package ru.crspet.fileserver.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static int getIdFromClient(String findMethod, DataInputStream is) throws IOException {
        int id;
        if (findMethod.equals("name")) {
            id = is.readUTF().hashCode();
        } else {
            id = is.readInt();
        }
        return id;
    }

}
