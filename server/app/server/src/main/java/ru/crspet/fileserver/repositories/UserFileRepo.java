package ru.crspet.fileserver.repositories;

import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public interface UserFileRepo {

    void save(String fileName, String filePath);
    String findById(Integer id);
    String findByFileName(String fileName);

    void deleteById(int id) throws IOException;

}