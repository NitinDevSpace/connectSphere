package dev.nitin.server.model.sub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String id;
    private String userId;
    private String userName;
    private String comment;
    private boolean isEdited;
    private boolean isDeleted;
    private int repliesCount;
    private Instant commentedAt;
    private List<ReplyToComment> repliesToComments;

    public Comment(String userId, String userName, String comment) {
        this.id = new ObjectId().toString();
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
        this.commentedAt = Instant.now();
        this.repliesToComments = new ArrayList<>();
        this.repliesCount = 0;
    }
}
