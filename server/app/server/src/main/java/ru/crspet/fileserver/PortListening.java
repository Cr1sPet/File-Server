package ru.crspet.fileserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.crspet.fileserver.utils.SerializationUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;


@Component
@PropertySource("server-app.properties")
public class PortListening {
    int PORT;

    public PortListening(@Value("${server.port}") int PORT) {
        this.PORT = PORT;
    }

    public void listen() {
        while (true) {
            try (ServerSocket server = new ServerSocket(PORT)) {
                Session session = new Session(server.accept());
                System.out.println("client accepted");
                session.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
