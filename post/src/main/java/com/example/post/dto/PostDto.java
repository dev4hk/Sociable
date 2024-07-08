package com.example.post.dto;

import com.example.post.entity.Post;
import com.example.post.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostDto {

    private Integer id;
    private String body;
    private String filePath;
    private String fileType;
    private Integer userId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;


    public static PostDto fromEntity(Post entity) {
        return new PostDto(
                entity.getId(),
                entity.getBody(),
                entity.getFilePath(),
                entity.getFileType(),
                entity.getUserId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
