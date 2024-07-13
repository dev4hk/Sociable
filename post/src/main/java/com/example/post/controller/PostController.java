package com.example.post.controller;

import com.example.post.dto.PostDto;
import com.example.post.response.PostResponse;
import com.example.post.response.Response;
import com.example.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<PostResponse> create(@RequestParam(name = "body", required = false) String body, @RequestParam(name = "file", required = false) MultipartFile file, @RequestHeader("Authorization") String token) {
        PostDto postDto = postService.create(body, file, token);
        return Response.success(PostResponse.fromPostDto(postDto));
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestParam("body") String body, @RequestParam(name = "file", required = false) MultipartFile file, @RequestHeader("Authorization") String token) {
        PostDto postDto = postService.modify(body, file, token, postId);
        return Response.success(PostResponse.fromPostDto(postDto));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, @RequestHeader("Authorization") String token) {
        postService.delete(token, postId);
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> getAllPosts(Pageable pageable, @RequestHeader("Authorization") String token) {
        return Response.success(postService.getAllPosts(pageable, token).map(PostResponse::fromPostDto));
    }

    @GetMapping("/user/{id}")
    public Response<Page<PostResponse>> getAllPostsByUserId(@PathVariable Integer id, Pageable pageable, @RequestHeader("Authorization") String token) {
        return Response.success(postService.getAllPostsByUserId(id, pageable, token).map(PostResponse::fromPostDto));
    }

    @GetMapping("/{postId}")
    public Response<PostResponse> getPostById(@PathVariable Integer postId, @RequestHeader("Authorization") String token) {
        return Response.success(PostResponse.fromPostDto(postService.getPostById(postId, token)));
    }

    @PatchMapping("/{postId}/like")
    public Response<PostResponse> likePost(@PathVariable Integer postId, @RequestHeader("Authorization") String token) {
        return Response.success(PostResponse.fromPostDto(postService.likeUnlikePost(postId, token)));
    }


}
