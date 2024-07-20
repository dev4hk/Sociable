package com.example.user.response;

import com.example.user.entity.User;
import com.example.user.model.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private FileInfo fileInfo;
    private String description;
    private Set<Integer> followings;
    private Set<Integer> followers;
    private Set<Integer> savedPosts;
    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getFileInfo(),
                user.getDescription(),
                user.getFollowings(),
                user.getFollowers(),
                user.getSavedPosts()
        );
    }
}
