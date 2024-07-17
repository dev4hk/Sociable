package com.example.user.controller;

import com.example.user.request.ChangePasswordRequest;
import com.example.user.request.ChangeUserInfoRequest;
import com.example.user.response.UserResponse;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/change/info")
    public ResponseEntity<?> changeUserInfo(@RequestBody @Valid ChangeUserInfoRequest request, Principal connectedUser) {
        return ResponseEntity.ok(UserResponse.fromUser(userService.changeUserInfo(request, connectedUser)));

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
    public ResponseEntity<List<UserResponse>> getOtherUsersInfo(Principal connectedUser) {
        return ResponseEntity.ok(this.userService.getOtherUsersInfo(connectedUser).stream().map(UserResponse::fromUser).toList());
    }

}

