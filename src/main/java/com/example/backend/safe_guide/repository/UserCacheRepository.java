package com.example.backend.safe_guide.repository;

import com.example.backend.safe_guide.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, User> userRedisTemplate;

    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);


    public void setUser(User user) {
        String key = getKey(user.getUserId());
        log.info("Set User to Redis {}({})", key, user);
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<User> getUser(String userId) {
        User data = userRedisTemplate.opsForValue().get(getKey(userId));
        log.info("Get User from Redis {}", data);
        return Optional.ofNullable(data);
    }


    private String getKey(String userId) {
        return "USER:" + userId;
    }
}
