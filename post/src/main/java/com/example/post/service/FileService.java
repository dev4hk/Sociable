package com.example.post.service;

import com.example.post.model.FileInfo;
import com.example.post.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "STORAGE-SERVICE", url = "http://localhost:8085")
public interface FileService {

    @PostMapping(value = "/api/v1/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<FileInfo> upload(MultipartFile file, @RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/files")
    Response<byte[]> download(@RequestParam("filePath") String filePath, @RequestHeader("Authorization") String token);
}
