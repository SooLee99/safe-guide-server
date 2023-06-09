package com.example.backend.safe_guide.model;

import com.example.backend.safe_guide.model.entity.LikeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Like {
    private Integer likeId;
    private Integer idToken;
    private String userId;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static Like fromEntity(LikeEntity entity) {
        return new Like(
                entity.getId(),
                entity.getUser().getIdToken(),
                entity.getUser().getUserId(),
                entity.getPost().getPostId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}
