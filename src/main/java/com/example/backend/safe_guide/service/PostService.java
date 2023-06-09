package com.example.backend.safe_guide.service;

import com.example.backend.safe_guide.exception.ErrorCode;
import com.example.backend.safe_guide.exception.SafeGuideApplicationException;
import com.example.backend.safe_guide.model.*;
import com.example.backend.safe_guide.model.entity.CommentEntity;
import com.example.backend.safe_guide.model.entity.LikeEntity;
import com.example.backend.safe_guide.model.entity.PostEntity;
import com.example.backend.safe_guide.model.entity.UserEntity;
import com.example.backend.safe_guide.producer.AlarmProducer;
import com.example.backend.safe_guide.repository.CommentEntityRepository;
import com.example.backend.safe_guide.repository.LikeEntityRepository;
import com.example.backend.safe_guide.repository.PostEntityRepository;
import com.example.backend.safe_guide.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final AlarmProducer alarmProducer;

    @Transactional
    public void create(String userId, String title, String body) {
        UserEntity userEntity = userEntityRepository.findByUserId(userId)
                .orElseThrow(() -> new SafeGuideApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userId is %s", userId)));
        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postEntityRepository.save(postEntity);
    }

    // entity mapping
    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(Integer userId, Pageable pageable) {
        return postEntityRepository.findAllByUserUserId(userId, pageable).map(Post::fromEntity);
    }

    @Transactional
    public Post modify(Integer userId, Integer postId, String title, String body) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SafeGuideApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        if (!Objects.equals(postEntity.getUser().getIdToken(), userId)) {
            throw new SafeGuideApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(Integer userId, Integer postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SafeGuideApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        if (!Objects.equals(postEntity.getUser().getIdToken(), userId)) {
            throw new SafeGuideApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, postId));
        }
        likeEntityRepository.deleteAllByPost(postEntity);
        commentEntityRepository.deleteAllByPost(postEntity);
        postEntityRepository.delete(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String userId, String comment) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SafeGuideApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        UserEntity userEntity = userEntityRepository.findByUserId(userId)
                .orElseThrow(() -> new SafeGuideApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userId is %s", userId)));

        commentEntityRepository.save(CommentEntity.of(comment, postEntity, userEntity));

        // create alarm
        // notificationService.send(AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postId), postEntity.getUser());
        alarmProducer.send(new AlarmEvent(AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getIdToken(), postId), postEntity.getUser().getIdToken()));
    }

    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SafeGuideApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        return commentEntityRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SafeGuideApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        UserEntity userEntity = userEntityRepository.findByUserId(userId)
                .orElseThrow(() -> new SafeGuideApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userId is %s", userId)));

        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SafeGuideApplicationException(ErrorCode.ALREADY_LIKED_POST, String.format("userId %s already like the post %s", userId, postId));
        });

        likeEntityRepository.save(LikeEntity.of(postEntity, userEntity));

        // create alarm
        //otificationService.send(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postId), postEntity.getUser());
        alarmProducer.send(new AlarmEvent(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getIdToken(), postId), postEntity.getUser().getIdToken()));
    }

    public Integer getLikeCount(Integer postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SafeGuideApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        List<LikeEntity> likes = likeEntityRepository.findAllByPost(postEntity);
        return likes.size();
    }

}
