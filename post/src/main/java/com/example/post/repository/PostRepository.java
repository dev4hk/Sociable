package com.example.post.repository;

import com.example.post.entity.Post;
import com.example.post.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByUserId(Integer id, Pageable pageable);
}
