package dev.nitin.server.dto.reaction;

import dev.nitin.server.model.sub.Reaction;
import dev.nitin.server.model.sub.ReactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionRequestDto {
    private String userId;
    private String userName;
    @NotNull
    private ReactionType reactionType;

    public Reaction toEntity() {
        return new Reaction(userId, userName, reactionType);
    }
}
