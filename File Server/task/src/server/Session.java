package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session extends Thread {
    private Socket socket;

    Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String response;
        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ){
            System.out.println("into run");
            while (true) {
                System.out.println("start to wait info from client");
                String message = inputStream.readUTF();
                System.out.println("Received: " + message);
                String []parsedMessage = message.stripLeading().split("\\s", 3);
                switch (parsedMessage[0]) {
                    case "PUT":
                        response = QueryProcessing.PutProcess(parsedMessage, inputStream);
                        outputStream.writeUTF(response);
                        break;
                    case "GET":
                        response = QueryProcessing.GetProcess(parsedMessage);
                        outputStream.writeUTF(response);
                        break;
                    case "DELETE":
                        response = QueryProcessing.DeleteProcess(parsedMessage);
                        outputStream.writeUTF(response);
                        break;
                    case "exit":
                        outputStream.write(200);
                        socket.close();
                        System.exit(0);
                }
                System.out.println("Sent: " + "All files were sent!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
