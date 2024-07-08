package com.example.post.fixture;

import com.example.post.entity.Post;
import com.example.post.model.User;

public class PostFixture {
    public static Post get(Integer postId, Integer userId) {
        Post result = new Post();
        result.setUserId(userId);
        result.setId(postId);

        return result;
    }
}
