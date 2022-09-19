package ru.crspet.fileserver.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.crspet.fileserver.utils.SerializationUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component("userFileRepoImpl")
@PropertySource("server-app.properties")
public class UserFileRepoImpl implements UserFileRepo {

    @Value("${server.data-dir}")
    private String userFileSetFilePath;

    public static String FILES_PATH;

    Map<Integer, String> userFileMap = null;


    @PostConstruct
    public void construct() {
        FILES_PATH = userFileSetFilePath + "files/";
        new File(FILES_PATH).mkdirs();

        userFileSetFilePath += "serialized/";
        new File(userFileSetFilePath).mkdirs();
        userFileSetFilePath += "SerializedObjects";
        try {
            if ( new File(userFileSetFilePath).exists()) {
                userFileMap = (Map<Integer, String>) SerializationUtils.deserialize(userFileSetFilePath);
            }
            if (userFileMap == null) {
                userFileMap = new HashMap<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String fileName, String filePath) {
        userFileMap.put(fileName.hashCode(), filePath);
        try {
            SerializationUtils.serialize(userFileMap, userFileSetFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findById(Integer id) {
        return userFileMap.get(id);
    }

    @Override
    public String findByFileName(String fileName) {
        return userFileMap.get(fileName.hashCode());
    }

    @Override
    public void deleteById(int id) throws IOException {
        if (userFileMap.containsKey(id)) {
            userFileMap.remove(id, userFileMap.get(id));
            SerializationUtils.serialize(userFileMap, userFileSetFilePath);
        }
    }
}
