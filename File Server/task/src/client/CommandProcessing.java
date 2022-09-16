package client;

import java.io.*;
import java.nio.charset.IllegalCharsetNameException;

public class CommandProcessing  {

    public static final String getFileNamePrompt = "Enter name of the file: ";
    public static final String getFileNameToSavePrompt = "Enter name of the file to be saved on server:";

    public static final String clientDataPack = System.getProperty("user.dir") +
            File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;

    public byte[] getByteArrayFromFile(String fileName) throws IOException {

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


    public void createAFile(DataInputStream inputStream, DataOutputStream outputStream, BufferedReader reader) throws IOException {
        String request = "PUT";
        outputStream.writeUTF(request); // PUT
        System.out.print(getFileNamePrompt);
        String fileNameToRead = clientDataPack + reader.readLine();
        System.out.print(getFileNameToSavePrompt);
        outputStream.writeUTF(reader.readLine()); // FileName
        if (403 == inputStream.readInt()) {
            System.out.println("The response says that creating the file was forbidden!");
            return;
        }
        byte[] bytes = getByteArrayFromFile(fileNameToRead);
        outputStream.writeInt(bytes.length); // Bytes Length
        outputStream.write(bytes); // Bytes

        System.out.println("The request was sent.");


        String response = inputStream.readUTF();
        String []parsedResponse = response.split("\\s");

        switch (parsedResponse[0]) {
            case "200" :
                System.out.println("Response says that file is saved! ID = " + parsedResponse[1]);
                break;
            case "403" :
                System.out.println("The response says that creating the file was forbidden!");
                break;
            case "500" :
                System.out.println("The response says that there is an unexpected error!");
                break;
        }
    }

    public void stopServer(DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF("exit");
//        System.out.println("The request was sent.");

        if (inputStream.read() != 200) {
            System.out.println("error in server in exit");
        }
    }


    public void writeBytesToFIle(byte []bytes,  File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
    }

    public void getAFile(DataInputStream inputStream, DataOutputStream outputStream, BufferedReader reader) throws IOException {
        Get get = new Get();
        System.out.print("Do you want to get file by name or id (1 - name, 2 - id): ");

        int item;

        try {
            item = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e ) {
            System.out.println("Invalid input, please try again");
            return ;
        }
        switch(item) {
            case 1:
                System.out.print(getFileNamePrompt);
                get.setFileRecognizer("name");
                get.setName(reader.readLine());
                break;
            case 2:
                System.out.print("Enter id: ");
                get.setFileRecognizer("id");
                try {
                    get.setId(Integer.parseInt(reader.readLine()));
                } catch (NumberFormatException e) {
                    System.out.println("error: invalid id");
                    return;
                }
                break;
        }

        outputStream.writeUTF(Get.type); // GET
        outputStream.writeUTF(get.getFileRecognizer()); //name or id
        if (get.getFileRecognizer().equals("name")) {
            outputStream.writeUTF(get.getName()); // file name to search
        } else {
            outputStream.writeInt(get.getId()); // file id to search
        }

        System.out.println("The request was sent.");

        Integer responseCode = inputStream.readInt();
        System.out.println("Response Code " + responseCode);
        switch (responseCode) {
            case 200 :
                int Lt = inputStream.readInt();
                byte[] bytes = new byte[Lt];
                System.out.println("Before ReadFully bytes size : " + bytes.length);
                inputStream.readFully (bytes, 0, bytes.length);
                System.out.println("The file was downloaded! Specify a name for it: ");
                writeBytesToFIle(bytes, new File(clientDataPack + reader.readLine()));
                System.out.println("File saved on the hard drive!");
                break;
            case 403 :
                System.out.println("The response says that this file is not found!");
                break;
            case 500 :
                System.out.println("The response says that there is an unexpected error!");
                break;
        }
    }

    public void deleteAFile(DataInputStream inputStream, DataOutputStream outputStream, BufferedReader reader) throws IOException {
        Get get = new Get();
        System.out.print("Do you want to delete file by name or id (1 - name, 2 - id): ");

        int item;

        try {
            item = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e ) {
            System.out.println("Invalid input, please try again");
            return ;
        }
        switch(item) {
            case 1:
                System.out.print(getFileNamePrompt);
                get.setFileRecognizer("name");
                get.setName(reader.readLine());
                break;
            case 2:
                System.out.print("Enter id: ");
                get.setFileRecognizer("id");
                try {
                    get.setId(Integer.parseInt(reader.readLine()));
                } catch (NumberFormatException e) {
                    System.out.println("error: invalid id");
                    return;
                }
                break;
        }


        outputStream.writeUTF("DELETE"); // GET
        outputStream.writeUTF(get.getFileRecognizer()); //name or id
        if (get.getFileRecognizer().equals("name")) {
            outputStream.writeUTF(get.getName()); // file name to search
        } else {
            outputStream.writeInt(get.getId()); // file id to search
        }

        System.out.println("The request was sent.");

        String response = inputStream.readUTF();

        switch (response) {
            case "200" :
                System.out.println("The response says that the file was successfully deleted!");
                break;
            case "404" :
                System.out.println("The response says that the file was not found!");
                break;
            case "500" :
                System.out.println("The response says that there is an unexpected error!");
                break;
        }
    }

}
