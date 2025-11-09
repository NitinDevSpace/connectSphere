package dev.nitin.server.dto.comment;

import dev.nitin.server.model.sub.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    private  String userId;
    private  String userName;
    @NotBlank
    private  String comment;

    public Comment toEntity() {
        return new Comment(userId, userName, comment);
    }
}
