package com.dev.main.service;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String save(MultipartFile file);
    void deleteIfExists(String storedName);
    boolean isExist(String filename);
    Path getPath(String storedName);
    Path getRoot();
}
