package com.example.user.service;

import com.example.user.config.JwtService;
import com.example.user.entity.User;
import com.example.user.enums.ErrorCode;
import com.example.user.exception.UserException;
import com.example.user.model.FileInfo;
import com.example.user.repository.UserRepository;
import com.example.user.request.ChangePasswordRequest;
import com.example.user.request.ChangeUserInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final FileService fileService;
    private static final String AUTH_PREFIX = "Bearer ";

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UserException(ErrorCode.BAD_CREDENTIAL, "Wrong Password");
        }
        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new UserException(ErrorCode.INVALID_REQUEST, "Passwords are not matching");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public User getUserProfile(String token) {
        String jwt = token.substring(AUTH_PREFIX.length());
        String username = this.jwtService.extractUsername(jwt);
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "User Not Found"));
    }

    public User getOtherUserInfo(Integer id, String token) {
        this.getUserProfile(token);
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "User Not Found"));
    }
    public List<User> getOtherUsersInfo(String query, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return this.userRepository.findOtherUsers(user.getId(), query);
    }

    public User changeUserInfo(ChangeUserInfoRequest request, MultipartFile image, Principal connectedUser, String token) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setDescription(request.getDescription());
        if(image != null && !image.isEmpty()) {
            FileInfo fileInfo = fileService.upload(image, token).getResult();
            user.setFileInfo(fileInfo);
        }
        return userRepository.save(user);
    }

    @Transactional
    public void followUnfollowUser(Principal connectedUser, Integer userId) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        var otherUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        if(!user.getFollowings().contains(userId)) {
            followUser(user, otherUser);
        }
        else {
            unfollowUser(user, otherUser);
        }
        userRepository.save(user);
        userRepository.save(otherUser);
    }

    public List<User> getFollowings(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return userRepository.getFollowings(user.getFollowings());
    }

    public List<User> getFollowers(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return userRepository.getFollowers(user.getFollowers());
    }

    private void followUser(User from, User to) {
        from.getFollowings().add(to.getId());
        to.getFollowers().add(from.getId());
    }

    private void unfollowUser(User from, User to) {
        from.getFollowings().remove(to.getId());
        to.getFollowers().remove(from.getId());
    }

    public void saveUnsavePost(Integer postId, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if(user.getSavedPosts().contains(postId)) {
            user.getSavedPosts().remove(postId);
        }
        else {
            user.getSavedPosts().add(postId);
        }
    }
}