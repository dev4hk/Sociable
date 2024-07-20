package com.example.post.repository;

import com.example.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByUserId(Integer id, Pageable pageable);

    @Query("SELECT p from Post p WHERE p.id in :savedPosts")
    Page<Post> findAllSavedPosts(@Param("savedPosts") Set<Integer> savedPosts, Pageable page);
}
