package dev.nitin.server.service;

import dev.nitin.server.dto.reaction.ReactionRequestDto;
import dev.nitin.server.exception.ResourceNotFoundException;
import dev.nitin.server.model.Post;
import dev.nitin.server.model.sub.Reaction;
import dev.nitin.server.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final PostRepository postRepository;

//    Likes
    public Set<Reaction> getReactions(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        return post.getReactions();
    }


    public void addLikeToPost(String postId, ReactionRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.getReactions().removeIf(r -> r.getUserId().equals(dto.getUserId())); // remove old
        post.getReactions().add(dto.toEntity());
        post.setReactionsCount(post.getReactions().size());

        postRepository.save(post);
    }

    public void removeLikeFromPost(String postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.getReactions().removeIf(reaction -> reaction.getUserId().equals(userId));
        post.setReactionsCount(post.getReactions().size());
        postRepository.save(post);
    }
}
