package com.example.user.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j
public class FileUtils {
    private static final String Directory = System.getProperty("user.home") + "/Downloads/uploads/";

    public static Map<String, String> upload(Integer userId, MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();
        String contentType = file.getContentType();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String filePath = Directory + userId;
        Files.createDirectories(Paths.get(filePath));
        copy(file.getInputStream(), Paths.get(filePath + "/" + fileName), REPLACE_EXISTING);
        map.put("filePath", filePath + "/" + fileName);
        map.put("contentType", contentType);
        return map;
    }

    public static byte[] readFileFromLocation(String fileUrl) {
        if (io.micrometer.common.util.StringUtils.isBlank(fileUrl)) {
            return null;
        }
        try {
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);
        } catch (IOException ex) {
            log.warn("No file found in the path {}", fileUrl);
            throw new RuntimeException("Resource Not found");
        }
    }
}
