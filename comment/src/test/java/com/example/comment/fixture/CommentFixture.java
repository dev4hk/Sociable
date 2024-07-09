package com.example.comment.fixture;

import com.example.comment.entity.Comment;
import com.example.comment.model.Post;

public class CommentFixture {
    public static Comment get(Integer commentId, Integer userId, String body) {
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setUserId(userId);
        comment.setComment(body);
        return comment;
    }
}
