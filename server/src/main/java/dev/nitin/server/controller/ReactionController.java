package dev.nitin.server.controller;

import dev.nitin.server.dto.reaction.ReactionRequestDto;
import dev.nitin.server.dto.reaction.ReactionResponseDto;
import dev.nitin.server.security.CustomUserPrincipal;
import dev.nitin.server.service.ReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/posts/{postId}/likes")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;

    //getAll Reactions
    @GetMapping()
    public ResponseEntity<Set<ReactionResponseDto>> getReactions(@PathVariable String postId) {
        Set<ReactionResponseDto> reactions =  reactionService.getReactions(postId).stream()
                .map(ReactionResponseDto::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(reactions);
    }

    //Add Reaction
    @PostMapping()
    public ResponseEntity<String> addReaction(@PathVariable String postId,
                                              @Valid @RequestBody ReactionRequestDto reactionRequestDto) {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userPrincipal.userId();
        String userName = userPrincipal.userName();
        reactionRequestDto.setUserId(userId);
        reactionRequestDto.setUserName(userName);

        reactionService.addLikeToPost(postId, reactionRequestDto);
        return ResponseEntity.ok("Reacted to the post");
    }

    //Remove Reaction
    @DeleteMapping()
    public ResponseEntity<String> removeReaction(@PathVariable String postId) {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userPrincipal.userId();
        reactionService.removeLikeFromPost(postId, userId);
        return ResponseEntity.ok("Reaction removed from the post");
    }
}
