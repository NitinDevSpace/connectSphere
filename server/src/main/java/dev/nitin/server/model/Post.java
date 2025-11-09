package dev.nitin.server.model;

import dev.nitin.server.model.sub.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private String userId;
    private String userName;
    private String userProfilePic;
    private String caption;
    private String fileType;
    private String contentLink;
    @Builder.Default
    private boolean isDeleted = false;
    @Builder.Default
    private boolean isEdited = false;
    @Builder.Default
    private boolean isPinned = false;
    @Builder.Default
    private boolean isArchived = false;
    @Transient
    private ReactionType userReaction = null;
    @Builder.Default
    private int reactionsCount = 0;
    @Builder.Default
    private int commentsCount = 0;
    @Builder.Default
    private int sharesCount = 0;
    @Builder.Default
    private int viewsCount = 0;
    @Builder.Default
    private Visibility visibility = Visibility.PUBLIC;
    @Builder.Default
    private Map<ReactionType, Integer> reactionTypeCount = new EnumMap<>(ReactionType.class);
    @Builder.Default
    private Set<Reaction> reactions = new HashSet<>();
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
    @Builder.Default
    private List<TaggedUsers> taggedUsers = new ArrayList<>();
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();
    @GeoSpatialIndexed
    private GeoJsonPoint location;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant editedAt;
}
