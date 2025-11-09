package dev.nitin.server.model.sub;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class TaggedUsers {
    private String userId;
    private String name;
    @CreatedDate
    private LocalDateTime taggedAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
