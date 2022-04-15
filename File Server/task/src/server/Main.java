package server;

public class Main {
    private static final int PORT = 23456;
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Server started!");
        new PortListening(PORT).start();
    }
}
