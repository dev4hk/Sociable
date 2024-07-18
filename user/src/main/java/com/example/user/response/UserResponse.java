package com.example.user.response;

import com.example.user.entity.User;
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
    private String filePath;
    private String contentType;
    private String description;
    private Set<Integer> followings;
    private Set<Integer> followers;
    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getFilePath(),
                user.getContentType(),
                user.getDescription(),
                user.getFollowings(),
                user.getFollowers()
        );
    }
}
