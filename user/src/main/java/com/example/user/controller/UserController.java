package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.request.ChangePasswordRequest;
import com.example.user.request.ChangeUserInfoRequest;
import com.example.user.response.UserResponse;
import com.example.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService userService;

    @PatchMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request, @AuthenticationPrincipal User user) {
        userService.changePassword(request, user);
        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = "/change/info")
    public ResponseEntity<?> changeUserInfo(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("request") String request,
            @AuthenticationPrincipal User user,
            @RequestHeader("Authorization") String token
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ChangeUserInfoRequest changeRequest = objectMapper.readValue(request, ChangeUserInfoRequest.class);
        return ResponseEntity.ok(UserResponse.fromUser(userService.changeUserInfo(
                file,
                changeRequest,
                user,
                token)));

    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse>  getUserProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                UserResponse.fromUser(
                        user
                ));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserResponse>  getOtherUserInfo(@PathVariable Integer id) {
        return ResponseEntity.ok(
                UserResponse.fromUser(
                        this.userService.getOtherUserInfo(id)
                ));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getOtherUsersInfo(@RequestParam String query, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(this.userService.getOtherUsersInfo(query, user).stream().map(UserResponse::fromUser).toList());
    }

    @PutMapping("/follow/{userId}")
    public ResponseEntity<UserResponse> followUser(@PathVariable Integer userId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(UserResponse.fromUser(this.userService.followUser(user, userId)));
    }

    @PutMapping("/post/save/{postId}")
    public ResponseEntity<UserResponse> savePost(@PathVariable Integer postId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(UserResponse.fromUser(this.userService.savePost(postId, user)));

    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<UserResponse>> getUserSuggestions(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(this.userService.getUserSuggestions(user).stream().map(UserResponse::fromUser).toList());
    }

}

