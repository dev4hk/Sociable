package com.example.post.response;

import com.example.post.dto.PostDto;
import com.example.post.model.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Integer id;
    private String body;
    private FileInfo fileInfo;
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
                postDto.getFileInfo(),
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