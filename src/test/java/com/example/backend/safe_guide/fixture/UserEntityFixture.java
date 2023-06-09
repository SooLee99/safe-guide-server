package com.example.backend.safe_guide.fixture;

import com.example.backend.safe_guide.model.UserRole;
import com.example.backend.safe_guide.model.entity.UserEntity;

import java.sql.Timestamp;
import java.time.Instant;

public class UserEntityFixture {

    public static UserEntity get(String userId, String password) {
        UserEntity entity = new UserEntity();
        entity.setIdToken(1);
        entity.setUserId(userId);
        entity.setPassword(password);
        entity.setRole(UserRole.USER);
        entity.setRegisteredAt(Timestamp.from(Instant.now()));
        return entity;
    }
}
