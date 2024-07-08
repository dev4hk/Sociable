package com.example.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {
    public Object create(Integer postId, String comment, String testToken) {
        return null;
    }

    public Object findAllByPostId(Integer postId, Pageable pageable) {
        return null;
    }
}
