package dev.nitin.server.service;

import dev.nitin.server.dto.post.PostRequestDto;
import dev.nitin.server.dto.post.PostResponseDto;
import dev.nitin.server.exception.ResourceNotFoundException;
import dev.nitin.server.model.Post;
import dev.nitin.server.model.sub.Reaction;
import dev.nitin.server.repository.PostRepository;
import dev.nitin.server.service.fileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;

    public Post createPost(PostRequestDto dto, MultipartFile file) {
        // Basic validation
        if ((dto.getCaption() == null || dto.getCaption().isBlank()) &&
                (file == null || file.isEmpty()))
            throw new IllegalArgumentException("Post must have caption or media");

        // 🔹 Create Post entity from DTO
        Post post = Post.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .caption(dto.getCaption())
                .build();

        // 🔹 If there’s a file, upload it and save URL + type
        //System.out.println(">>> FileStorageService bean in use: " + fileStorageService.getClass().getSimpleName());
        if (file != null && !file.isEmpty()) {
            String fileUrl = fileStorageService.saveFile(file);
            post.setContentLink(fileUrl);
            post.setFileType(file.getContentType());
        }

        // 🔹 Save the post to MongoDB
        return postRepository.save(post);
    }

    public Post updatePost(String postId, PostRequestDto updatedPostDto, MultipartFile file) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if ((file == null) && (updatedPostDto.getCaption() == null || updatedPostDto.getCaption().isBlank())) {
            throw new IllegalArgumentException("Post must have caption or media");
        }
        post.setEdited(true);
        post.setCaption(updatedPostDto.getCaption());

        if (file != null && !file.isEmpty()) {
            String fileUrl = fileStorageService.saveFile(file);
            post.setContentLink(fileUrl);
            post.setFileType(file.getContentType());
        }

        return postRepository.save(post);
    }

    public void deletePost(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

       post.setDeleted(true);
         postRepository.save(post);
    }

    public Post findPostById(String postId) {
        return  postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    public List<Post> getAllPosts(String userId) {
        return postRepository.findAllByUserId(userId);
    }

    //Feed
    public List<Post> getRecentPosts(int limit, int offset, String currentUserId) {
        List<Post> posts = postRepository.findTopNByOrderByCreatedAtDesc(limit, offset);

        for(Post post : posts) {
            post.setUserReaction(post.getReactions().stream()
                                .filter(reaction -> reaction.getUserId().equals(currentUserId))
                                .map(Reaction::getReactionType)
                                .findFirst()
                                .orElse(null)
            );
        }

        return  posts;
    }

}
