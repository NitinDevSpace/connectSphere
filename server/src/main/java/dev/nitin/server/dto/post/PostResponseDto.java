package dev.nitin.server.dto.post;

import dev.nitin.server.model.Post;
import dev.nitin.server.model.sub.ReactionType;
import dev.nitin.server.model.sub.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private String id;
    private String userId;
    private  String userName;
    private String caption;
    private String contentLink;
    private String fileType;
    private boolean isEdited;
    private boolean isPinned;
    private boolean isArchived;
    private ReactionType userReaction;
    private int reactionsCount;
    private int commentsCount;
    private int sharesCount;
    private int viewsCount;
    private Visibility visibility;
    private Map<ReactionType, Integer> reactionTypeCount;
    private GeoJsonPoint location;
    private Instant createdAt;

    public static PostResponseDto toDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getUserId(),
                post.getUserName(),
                post.getCaption(),
                post.getContentLink(),
                post.getFileType(),
                post.isEdited(),
                post.isPinned(),
                post.isArchived(),
                post.getUserReaction(),
                post.getReactionsCount(),
                post.getCommentsCount(),
                post.getSharesCount(),
                post.getViewsCount(),
                post.getVisibility(),
                post.getReactionTypeCount(),
                post.getLocation(),
                post.getCreatedAt()
        );

    }
}
