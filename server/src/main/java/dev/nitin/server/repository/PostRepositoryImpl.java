package dev.nitin.server.repository;

import dev.nitin.server.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.core.query.Query;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Post> findTopNByOrderByCreatedAtDesc(int limit, int offset) {
        Query query = new Query()
                .addCriteria(Criteria.where("isDeleted").is(false))
                .with(Sort.by(Sort.Direction.DESC, "createdAt"))
                .limit(limit)
                .skip(offset);

        return mongoTemplate.find(query, Post.class);
    }
}
