package com.example.post.response;

import com.example.post.dto.PostDto;
import com.example.post.model.User;
import com.example.post.util.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Integer id;
    private String body;
    private String filePath;
    private String fileType;
    private Integer userId;
    private String firstname;
    private String lastname;
    private Set<Integer> likedBy;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static PostResponse fromPostDto(PostDto postDto) {
        return new PostResponse(
                postDto.getId(),
                postDto.getBody(),
                postDto.getFilePath(),
                postDto.getFileType(),
                postDto.getUserId(),
                postDto.getFirstname(),
                postDto.getLastname(),
                postDto.getLikedBy(),
                postDto.getRegisteredAt(),
                postDto.getUpdatedAt(),
                postDto.getDeletedAt()
        );
    }
}