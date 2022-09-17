package ru.crspet.fileserver;

public enum Request {

    GET("GET"), PUT("PUT"), DELETE("DELETE");

    public final String label;

    Request(String label) {
        this.label = label;
    }

}
