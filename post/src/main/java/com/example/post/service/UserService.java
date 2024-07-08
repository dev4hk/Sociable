package com.example.post.service;

import com.example.post.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE", url = "http://localhost:8080")
public interface UserService {

    @GetMapping("/api/users/profile")
    ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt);
}
