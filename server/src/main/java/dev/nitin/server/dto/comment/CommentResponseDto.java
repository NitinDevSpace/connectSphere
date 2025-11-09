package dev.nitin.server.dto.comment;

import dev.nitin.server.model.sub.Comment;
import dev.nitin.server.model.sub.ReplyToComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private String id;
    private String userId;
    private String userName;
    private String comment;
    private int repliesToComments;
    private List<ReplyToComment> replies;
    private Instant commentedAt;

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUserId(),
                comment.getUserName(),
                comment.getComment(),
                comment.getRepliesToComments().size(),
                comment.getRepliesToComments(),
                comment.getCommentedAt()
        );
    }
}
