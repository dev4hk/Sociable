package com.example.comment.service;

import org.reactivestreams.Publisher;

public interface UserService {
    Iterable<? extends Publisher<?>> getUserProfile(String testToken);
}
