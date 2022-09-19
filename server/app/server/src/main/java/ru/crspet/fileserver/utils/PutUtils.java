package ru.crspet.fileserver.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PutUtils {

    public static void writeBytesToFile(byte []bytes, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(bytes);
        }
    }

}
