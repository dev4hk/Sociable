package com.example.comment.service;

import org.reactivestreams.Publisher;

public interface PostService {
    Iterable<? extends Publisher<?>> getPostById(Integer postId);
}
