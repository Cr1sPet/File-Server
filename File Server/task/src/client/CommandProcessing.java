package client;

import java.io.*;

public class CommandProcessing  {

    public static final String getFileNamePrompt = "Enter name of the file: ";
    public static final String getFileNameToSavePrompt = "Enter name of the file to be saved on server:";
    public static final String getFileContentPrompt = "Enter file content: ";


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

        String request = "PUT ";

        System.out.print(getFileNamePrompt);
        String fileNameToRead =  reader.readLine();

        System.out.print(getFileNameToSavePrompt);

        byte[] bytes = getByteArrayFromFile(fileNameToRead);

        request = request.concat(reader.readLine())
                .concat(" ")
                .concat(bytes.length + "");

        //sending header in format : PUT + fileName + fileSize
        outputStream.writeUTF(request);

        //sending file data in bytes
        outputStream.write(bytes);

        System.out.println("The request was sent.");

        String responseCode = inputStream.readUTF();
        System.out.println(responseCode);

        switch (responseCode) {
            case "200" :
                System.out.println("The response says that file was created!");
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
        System.out.println("The request was sent.");

        if (inputStream.read() == 200) {
            System.out.println("the response says that the server has been stopped");
        }
    }

    public void getAFile(DataInputStream inputStream, DataOutputStream outputStream, BufferedReader reader) throws IOException {
        String request = "GET ";

        System.out.print(getFileNamePrompt);
        request = request.concat(reader.readLine());

        outputStream.writeUTF(request);
        System.out.println("The request was sent.");

        String response = inputStream.readUTF();
        String []parsedResponse = response.split("\\s", 2);

        switch (parsedResponse[0]) {
            case "200" :
                System.out.println("The content of the file is: " + parsedResponse[1]);
                break;
            case "403" :
                System.out.println("The response says that the file was not found!");
                break;
            case "500" :
                System.out.println("The response says that there is an unexpected error!");
                break;
        }
    }

    public void deleteAFile(DataInputStream inputStream, DataOutputStream outputStream, BufferedReader reader) throws IOException {
        String request = "DELETE ";

        System.out.print(getFileNamePrompt);
        request = request.concat(reader.readLine());

        outputStream.writeUTF(request);
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
