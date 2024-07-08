package com.example.comment.fixture;


import com.example.comment.model.Post;

public class PostFixture {
    public static Post get(Integer postId) {
        Post result = new Post();
        result.setId(postId);

        return result;
    }
}
