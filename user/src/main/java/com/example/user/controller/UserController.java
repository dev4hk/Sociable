package com.example.user.controller;

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
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request, Principal connectedUser) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = "/change/info")
    public ResponseEntity<?> changeUserInfo(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("request") String request,
            Principal connectedUser,
            @RequestHeader("Authorization") String token
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ChangeUserInfoRequest changeRequest = objectMapper.readValue(request, ChangeUserInfoRequest.class);
        return ResponseEntity.ok(UserResponse.fromUser(userService.changeUserInfo(
                file,
                changeRequest,
                connectedUser,
                token)));

    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse>  getUserProfile(@RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                UserResponse.fromUser(
                        this.userService.getUserProfile(token)
                ));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserResponse>  getOtherUserInfo(@PathVariable Integer id, @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                UserResponse.fromUser(
                        this.userService.getOtherUserInfo(id, token)
                ));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getOtherUsersInfo(@RequestParam String query, Principal connectedUser) {
        return ResponseEntity.ok(this.userService.getOtherUsersInfo(query, connectedUser).stream().map(UserResponse::fromUser).toList());
    }

    @PutMapping("/follow/{userId}")
    public ResponseEntity<Void> followUser(@PathVariable Integer userId, Principal connectedUser) {
        this.userService.followUser(connectedUser, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/post/save/{postId}")
    public ResponseEntity<Void> savePost(@PathVariable Integer postId, Principal connectedUser) {
        this.userService.savePost(postId, connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<UserResponse>> getUserSuggestions(Principal connectedUser) {
        return ResponseEntity.ok(this.userService.getUserSuggestions(connectedUser).stream().map(UserResponse::fromUser).toList());
    }

}

