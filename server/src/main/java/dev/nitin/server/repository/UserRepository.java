package dev.nitin.server.repository;

import dev.nitin.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
}
