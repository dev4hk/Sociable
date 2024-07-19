package com.example.storage.service;

import com.example.storage.enums.ErrorCode;
import com.example.storage.exception.FileException;
import com.example.storage.model.User;
import com.example.storage.response.FileResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;

@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${application.file.upload.files-output-path}")
    private String fileUploadPath;

    private final UserService userService;

    public FileResponse upload(MultipartFile file, String token) {
        User user = null;
        try {
            user = this.userService.getUserProfile(token).getBody();
        } catch (Exception e) {
            throw new FileException(ErrorCode.INVALID_REQUEST, "Error getting user info");
        }

        if (file == null || file.isEmpty()) {
            return null;
        }
        String filePath = fileUploadPath + separator + "users" + separator + user.getId();
        File targetFolder = new File(filePath);
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                return null;
            }
        }
        final String fileExtension = getFileExtension(file.getOriginalFilename());
        String targetFilePath = filePath + separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath, file.getBytes());
        } catch (IOException e) {
        }
        return FileResponse.builder()
                .filePath(targetFilePath)
                .fileType(file.getContentType())
                .build();

    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    public byte[] download(String filePath, String token) {
        User user = null;
        try {
            user = this.userService.getUserProfile(token).getBody();
        } catch (Exception e) {
            throw new FileException(ErrorCode.INVALID_REQUEST, "Error getting user info");
        }
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        try {
            Path path = new File(filePath).toPath();
            return Files.readAllBytes(path);
        } catch (IOException ex) {

            throw new FileException(ErrorCode.FILE_NOT_FOUND, "File not found");
        }
    }
}
