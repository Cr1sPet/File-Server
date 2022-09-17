package ru.crspet.fileserver.utils;

import java.io.*;

public class GetUtils {
    public static   byte[] getByteArrayFromFile(String fileName) throws IOException {

        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + fileName + "doesn't exists!");
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

}
