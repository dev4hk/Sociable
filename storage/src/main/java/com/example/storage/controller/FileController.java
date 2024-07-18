package com.example.storage.controller;

import com.example.storage.response.FileResponse;
import com.example.storage.response.Response;
import com.example.storage.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileService fileService;

    @PostMapping
    public Response<FileResponse> upload(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        return Response.success(this.fileService.upload(file, token));
    }

    @GetMapping
    public Response<byte[]> download(@RequestParam("filePath") String filePath, @RequestHeader("Authorization") String token) {
        return Response.success(this.fileService.download(filePath, token));
    }
}
