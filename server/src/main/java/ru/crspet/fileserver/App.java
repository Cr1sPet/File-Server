package ru.crspet.fileserver;

/**
 * Hello world!
 *
 */
public class App {
    private static final int PORT = 23456;
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Server started!");
        new PortListening(PORT).listen();
    }
}
