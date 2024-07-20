package com.example.post.fixture;

import com.example.post.model.User;

import java.util.Set;

public class UserFixture {
    public static User get(Integer userId) {
        User result = new User();
        result.setId(userId);
        result.setSavedPosts(Set.of());
        return result;
    }
}
