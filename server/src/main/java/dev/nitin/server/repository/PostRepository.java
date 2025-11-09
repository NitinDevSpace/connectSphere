package dev.nitin.server.repository;

import dev.nitin.server.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String>, PostRepositoryCustom {
    List<Post> findAllByUserId(String userId);
}
