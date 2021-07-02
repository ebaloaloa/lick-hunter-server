package com.lickhunter.web.services.impl;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lickhunter.web.services.FileService;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Primary
@Service("fileServiceImpl")
public class FileServiceImpl<T, FILE> implements FileService<FILE, T> {

    @SneakyThrows
    public FILE readFromFile(String path, String filename, Class classType) {
        ObjectMapper mapper = new ObjectMapper();
        return (FILE) mapper.readValue(new File(path.concat(filename)), classType);
    }

    @SneakyThrows
    public void writeToFile(String path, String filename, T t) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(Paths.get(path.concat(filename)).toFile(), t);
    }
}
