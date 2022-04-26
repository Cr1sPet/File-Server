package client;

import java.io.*;

public class Activity {
    public static final String generalPrompt = "Enter action (1 - get a file, 2 - save a file, 3 - delete a file): ";
    public static void routine(DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ) {
            CommandProcessing commandProcessing = new CommandProcessing();
            while (true) {
                System.out.print(generalPrompt);
                switch (reader.readLine()) {
                    case "1" :
                        commandProcessing.getAFile(inputStream, outputStream, reader);
                        break;
                    case "2" :
                        commandProcessing.createAFile(inputStream, outputStream, reader);
                        break;
                    case "3" :
                        commandProcessing.deleteAFile(inputStream, outputStream, reader);
                        break;
                    case "exit" :
                        commandProcessing.stopServer(inputStream, outputStream);
                        System.exit(0);
                        break;
                    default :
                        System.out.println("Invalid input, please try again");
                        break;
                }
            }
        }
    }
}
