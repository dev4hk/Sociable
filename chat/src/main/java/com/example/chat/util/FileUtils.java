package com.example.chat.util;

import com.example.chat.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
@Component
public class FileUtils {

    private static final String Directory = System.getProperty("user.home") + "/Downloads/uploads/";

    public Map<String, String> upload(User user, Long chatId, MultipartFile file) throws IOException {
        Map<String, String> map = new HashMap<>();
        String contentType = file.getContentType();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String filePath = Directory + user.getId() + "/chat/" + chatId;
        Files.createDirectories(Paths.get(filePath));
        copy(file.getInputStream(), Paths.get(filePath + "/" + fileName), REPLACE_EXISTING);
        map.put("fileName", fileName);
        map.put("filePath", filePath + "/" + fileName);
        map.put("contentType", contentType);
        return map;
    }

    public byte[] readFileFromLocation(String fileUrl) {
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
