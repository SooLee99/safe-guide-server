package com.example.backend.safe_guide.controller.response;

import com.example.backend.safe_guide.model.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class LikeResponse {
    private Integer likeId;
    private Integer idToken;
    private String userId;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static LikeResponse fromLike(Like like) {
        return new LikeResponse(
                like.getLikeId(),
                like.getIdToken(),
                like.getUserId(),
                like.getPostId(),
                like.getRegisteredAt(),
                like.getUpdatedAt(),
                like.getRemovedAt()
        );
    }
}
