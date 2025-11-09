package dev.nitin.server.model.sub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyToComment {
    @Id
    private String id;
    private String userId;
    private String reply;
    private LocalDateTime repliedAt;

    public  ReplyToComment(String userId, String reply) {
        this.id = new ObjectId().toString();
        this.userId = userId;
        this.reply = reply;
        this.repliedAt = LocalDateTime.now();
    }
}
