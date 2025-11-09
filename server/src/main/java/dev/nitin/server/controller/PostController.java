package dev.nitin.server.controller;

import dev.nitin.server.dto.post.PostRequestDto;
import dev.nitin.server.dto.post.PostResponseDto;
import dev.nitin.server.dto.post.PostWithDataResponseDto;
import dev.nitin.server.model.Post;
import dev.nitin.server.security.CustomUserPrincipal;
import dev.nitin.server.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Post Service is up and running!");
    }

    // ✅ Handles JSON + optional file upload
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PostResponseDto> createPost(
            @RequestPart("post") PostRequestDto postRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        //System.out.println(">>> Controller received file: " + (file != null ? file.getOriginalFilename() : "null"));

        Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId;
        String userName;

//        System.out.println(">>> Principal object: " + principalObj);

        if (principalObj instanceof CustomUserPrincipal principal) {
            userId = principal.userId();
            userName = principal.userName();

        } else {
            throw new RuntimeException("Invalid authentication principal");
        }

        postRequestDto.setUserId(userId);
        postRequestDto.setUserName(userName);
        Post created = postService.createPost(postRequestDto, file);
        PostResponseDto postResponseDto = PostResponseDto.toDto(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostWithDataResponseDto> getPostById(@PathVariable String postId) {
       Post post = postService.findPostById(postId);
        return ResponseEntity.ok(PostWithDataResponseDto.toDto(post));
    }

    @GetMapping()
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        CustomUserPrincipal principal = (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.userId();
        List<PostResponseDto> posts = postService.getAllPosts(userId)
                .stream()
                .map(PostResponseDto::toDto)
                .toList();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/feed/recent")
    public ResponseEntity<List<PostResponseDto>> getRecentPosts(@RequestParam(defaultValue = "10") int limit,
                                                                @RequestParam(defaultValue = "0") int offset) {

        CustomUserPrincipal principal = (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.userId();

        List<PostResponseDto> posts = postService.getRecentPosts(limit, offset, userId)
                .stream()
                .map(PostResponseDto::toDto)
                .toList();
        return ResponseEntity.ok(posts);
    }

    @PatchMapping(value = "/{postId}", consumes = "multipart/form-data")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable String postId,
                                                      @RequestPart("post") PostRequestDto postRequestDto,
                                                      @RequestPart(value = "file", required = false) MultipartFile file) {
        Post updated = postService.updatePost(postId, postRequestDto, file);
        return ResponseEntity.ok(PostResponseDto.toDto(updated));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
