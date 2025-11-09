package dev.nitin.server.repository;

import dev.nitin.server.model.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findTopNByOrderByCreatedAtDesc(int limit, int offset);
}
