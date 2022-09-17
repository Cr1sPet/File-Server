package ru.crspet.fileserver;

public class Get {
    public static String type = "GET";
    private String name;

    private String fileRecognizer;
    private int id;


    public String getFileRecognizer() {
        return fileRecognizer;
    }

    public void setFileRecognizer(String fileRecognizer) {
        this.fileRecognizer = fileRecognizer;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        Get.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
