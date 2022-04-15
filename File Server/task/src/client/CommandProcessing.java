package client;

import java.io.*;

public class CommandProcessing  {

    public static final String getFileNamePrompt = "Enter filename: ";
    public static final String getFileContentPrompt = "Enter file content: ";

    public void createAFile(DataInputStream inputStream, DataOutputStream outputStream, BufferedReader reader) throws IOException {

        String request = "PUT ";

        System.out.print(getFileNamePrompt);
        request =   request.concat(reader.readLine()).concat(" ");
        System.out.print(getFileContentPrompt);
        request = request.concat(reader.readLine());

        outputStream.writeUTF(request);
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
