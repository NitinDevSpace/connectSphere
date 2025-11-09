package dev.nitin.server.service;

import dev.nitin.server.dto.comment.CommentRequestDto;
import dev.nitin.server.exception.ResourceNotFoundException;
import dev.nitin.server.model.Post;
import dev.nitin.server.model.sub.Comment;
import dev.nitin.server.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;

    //Comments
    public List<Comment> getAllCommentsForPost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        return post.getComments();
    }

    public void addCommentToPost(String postId, CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.getComments().add(commentRequestDto.toEntity());
        post.setCommentsCount(post.getComments().size());

        postRepository.save(post);
    }

    public void removeCommentFromPost(String postId, String commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.getComments().removeIf(comment -> comment.getId().equals(commentId));
        post.setCommentsCount(post.getComments().size());

        postRepository.save(post);
    }

    public Comment updateCommentInPost(String postId, String commentId, String newComment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Comment comment = post.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        comment.setComment(newComment);
        postRepository.save(post);

        return comment;
    }
}
