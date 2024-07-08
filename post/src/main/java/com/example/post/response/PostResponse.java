package com.example.post.response;

import com.example.post.dto.PostDto;
import com.example.post.util.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Integer id;
    private String body;
    private byte[] file;
    private String fileType;
    private Integer userId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static PostResponse fromPostDto(PostDto postDto) {
        return new PostResponse(
                postDto.getId(),
                postDto.getBody(),
                FileUtils.readFileFromLocation(postDto.getFilePath()),
                postDto.getFileType(),
                postDto.getUserId(),
                postDto.getRegisteredAt(),
                postDto.getUpdatedAt(),
                postDto.getDeletedAt()
        );
    }
}