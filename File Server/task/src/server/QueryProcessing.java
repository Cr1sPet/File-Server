package server;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class QueryProcessing  {
    private static final String testFilesPath = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;

    private static final String filesPath = "./File Server/task/src/server/data/";
    public static String PutProcess(String []parsedMessage) {
        try {
            String fileName = parsedMessage[1];
            File file = new File(filesPath + fileName);
            if (file.exists()) {
                System.out.println("File exist");
                return "403";
            } else {
                FileWriter writer = new FileWriter(file);
                String content = parsedMessage[2];
                writer.write(content);
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("IOException in PutProcess");
            e.printStackTrace();
            return "500";
        }
        return "200";
    }

    public static String GetProcess(String[] parsedMessage) {
        String fileContent = "";
        try {
            String fileName = parsedMessage[1];
            File file = new File(filesPath + fileName);
            if (!file.exists()) {
                System.out.println("File don't exist");
                return "403";
            } else {
                CharArrayWriter charArrayWriter;
                charArrayWriter = new CharArrayWriter();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String inputStr;
                while ((inputStr = reader.readLine()) != null) {
                    charArrayWriter.write(inputStr);
                }
                fileContent = charArrayWriter.toString();
                charArrayWriter.close();
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("IOException in PutProcess");
            e.printStackTrace();
            return "500";
        }
        return  "200 " + fileContent;
    }

    public static String DeleteProcess(String []parsedMessage) {
            String fileName = parsedMessage[1];
            String filePath = filesPath + fileName;
            try {
                if (!Files.deleteIfExists(Path.of(filePath))) {
                    return "404";
                }
            } catch (IOException e) {
                return "500";
            }
        return  "200";
    }
}
