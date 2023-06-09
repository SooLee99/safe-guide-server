package com.example.backend.safe_guide.model;

import com.example.backend.safe_guide.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Comment {
    private Integer commentId;
    private String comment;
    private Integer idToken;
    private String userId;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static Comment fromEntity(CommentEntity entity) {
        return new Comment(
                entity.getCommentId(),
                entity.getComment(),
                entity.getUser().getIdToken(),
                entity.getUser().getUserId(),
                entity.getPost().getPostId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}
