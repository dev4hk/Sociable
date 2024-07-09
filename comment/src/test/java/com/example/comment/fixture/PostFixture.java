package com.example.comment.fixture;


import com.example.comment.model.Post;

public class PostFixture {
    public static Post get(Integer postId, Integer userId) {
        Post result = new Post();
        result.setId(postId);
        result.setUserId(userId);
        return result;
    }
}
