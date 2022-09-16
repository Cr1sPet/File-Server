package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Session extends Thread {
    private Socket socket;
    private Map<Integer, String> MyFiles;
    Session(Socket socket, Map<Integer, String> MyFiles) {
        this.socket = socket;
        this.MyFiles = MyFiles;
    }

    @Override
    public void run() {
        String response;
        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        ){
            while (true) {
                String requestType = inputStream.readUTF();
                switch (requestType) {
                    case "PUT":
                        QueryProcessing.putProcess(MyFiles, inputStream, outputStream);
                        break;
                    case "GET":
                        QueryProcessing.GetProcess(inputStream, outputStream, MyFiles);
                        break;
                    case "DELETE":
                        response = QueryProcessing.DeleteProcess(inputStream, outputStream, MyFiles);
                        break;
                    case "exit":
                        outputStream.write(200);
                        socket.close();
                        System.exit(0);
                }
                System.out.println("Sent: " + "All files were sent!");
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Client was disconnected! " + e.getMessage());
            return;
        }
//        try {
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
