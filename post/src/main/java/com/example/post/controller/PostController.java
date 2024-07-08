package com.example.post.controller;

import com.example.post.dto.PostDto;
import com.example.post.request.PostCreateRequest;
import com.example.post.request.PostModifyRequest;
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
    public Response<PostResponse> create(@RequestBody PostCreateRequest request, @RequestHeader("Authorization") String token) {
        PostDto postDto = postService.create(request.getBody(), token);
        return Response.success(PostResponse.fromPostDto(postDto));
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, @RequestHeader("Authorization") String token) {
        PostDto postDto = postService.modify(request.getBody(), token, postId);
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

    @GetMapping("/my")
    public Response<Page<PostResponse>> getMyPosts(Pageable pageable, @RequestHeader("Authorization") String token) {
        return Response.success(postService.getMyPosts(pageable, token).map(PostResponse::fromPostDto));
    }

    @PostMapping(value = "/file/{postId}", consumes = "multipart/form-data")
    public Response<PostResponse> uploadFile(
            @PathVariable Integer postId,
            @RequestPart MultipartFile file,
            @RequestHeader("Authorization") String token
    ) {
        return Response.success(PostResponse.fromPostDto(postService.uploadFile(file, token, postId)));
    }

    @GetMapping("/{postId}")
    public Response<PostResponse> getPostById(@PathVariable Integer postId, @RequestHeader("Authorization") String token) {
        return Response.success(PostResponse.fromPostDto(postService.getPostById(postId, token)));
    }

}
