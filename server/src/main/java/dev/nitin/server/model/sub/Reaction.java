package dev.nitin.server.model.sub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {
    @Id
    private String id;
    private String userId;
    private String UserName;
    private ReactionType reactionType;
    private Instant reactedAt;
    private Instant updatedAt;

    public Reaction(String userId, String name, ReactionType reactionType) {
        this.id = new ObjectId().toString();
        this.userId = userId;
        this.UserName = name;
        this.reactionType = reactionType;
        this.reactedAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
