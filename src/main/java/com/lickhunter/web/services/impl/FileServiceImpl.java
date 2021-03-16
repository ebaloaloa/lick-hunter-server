package com.lickhunter.web.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lickhunter.web.services.FileService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Primary
@Service("fileServiceImpl")
public class FileServiceImpl<T, FILE> implements FileService<FILE, T> {

    public FILE readFromFile(String path, String filename, Class classType) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return (FILE) mapper.readValue(new File(path.concat(filename)), classType);
    }

    public void writeToFile(String path, String filename, T t) throws Exception {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(Paths.get(path.concat(filename)).toFile(), t);
    }
}
