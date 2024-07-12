package com.example.post.fixture;

import com.example.post.model.User;

public class UserFixture {
    public static User get(Integer userId) {
        User result = new User();
        result.setId(userId);

        return result;
    }
}
