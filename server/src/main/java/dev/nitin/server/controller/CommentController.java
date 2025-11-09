package dev.nitin.server.controller;

import dev.nitin.server.dto.comment.CommentRequestDto;
import dev.nitin.server.dto.comment.CommentResponseDto;
import dev.nitin.server.dto.comment.CommentUpdateDto;
import dev.nitin.server.model.sub.Comment;
import dev.nitin.server.security.CustomUserPrincipal;
import dev.nitin.server.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getAllComments(@PathVariable String postId) {
        List<CommentResponseDto> comments = commentService.getAllCommentsForPost(postId).stream()
                .map(CommentResponseDto::toDto)
                .toList();
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<String> addComment(@PathVariable String postId, @Valid @RequestBody CommentRequestDto commentRequestDto) {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentRequestDto.setUserId(userPrincipal.userId());

        commentRequestDto.setUserName(userPrincipal.userName());
        commentService.addCommentToPost(postId, commentRequestDto);
        return ResponseEntity.ok("Comment added to post");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> removeComment(@PathVariable String postId, @PathVariable String commentId) {
        commentService.removeCommentFromPost(postId, commentId);
        return ResponseEntity.ok("Comment removed from post");
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable String postId,
            @PathVariable String commentId,
            @Valid @RequestBody CommentUpdateDto commentUpdateDto) {

        Comment comment = commentService.updateCommentInPost(postId, commentId, commentUpdateDto.getComment());
        CommentResponseDto updated = CommentResponseDto.toDto(comment);

        return ResponseEntity.ok(updated);
    }

}
