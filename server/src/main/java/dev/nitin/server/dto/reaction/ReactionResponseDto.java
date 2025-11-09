package dev.nitin.server.dto.reaction;

import dev.nitin.server.model.sub.Reaction;
import dev.nitin.server.model.sub.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionResponseDto {
    private String userId;
    private String userName;
    private ReactionType reactionType;

    public static ReactionResponseDto toDto(Reaction reaction) {
       return new ReactionResponseDto(reaction.getUserId(), reaction.getUserName(), reaction.getReactionType());
    }
}
