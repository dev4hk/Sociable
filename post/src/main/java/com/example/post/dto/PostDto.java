package com.example.post.dto;

import com.example.post.entity.Post;
import com.example.post.model.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@AllArgsConstructor
public class PostDto {

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


    public static PostDto fromEntity(Post entity) {
        return new PostDto(
                entity.getId(),
                entity.getBody(),
                entity.getFileInfo(),
                entity.getUserId(),
                entity.getFirstname(),
                entity.getLastname(),
                entity.getLikedBy(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
