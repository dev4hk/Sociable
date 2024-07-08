package com.example.comment.fixture;

import com.example.comment.model.User;

public class UserFixture {
    public static User get(Integer userId) {
        User result = new User();
        result.setId(userId);

        return result;
    }
}
