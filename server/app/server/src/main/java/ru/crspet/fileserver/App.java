package ru.crspet.fileserver;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.crspet.fileserver.config.AppConfig;

public class App {
    public static void main(String[] args) {
        System.out.println("Server started!");

        try (
                AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
                ) {
            PortListening portListening = context.getBean(PortListening.class);
            System.out.println(portListening.PORT);
            portListening.listen();
        }
    }
}
