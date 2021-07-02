package com.lickhunter.web.services;

public interface FileService<FILE, T> {
    FILE readFromFile(String path, String filename, Class classType);
    void writeToFile(String path, String filename, T t);
}
