package com.example.backend.safe_guide.service;

import com.example.backend.safe_guide.exception.ErrorCode;
import com.example.backend.safe_guide.exception.SafeGuideApplicationException;
import com.example.backend.safe_guide.model.Alarm;
import com.example.backend.safe_guide.model.User;
import com.example.backend.safe_guide.utils.JwtTokenUtils;
import com.example.backend.safe_guide.model.entity.UserEntity;
import com.example.backend.safe_guide.repository.AlarmEntityRepository;
import com.example.backend.safe_guide.repository.UserEntityRepository;
import com.example.backend.safe_guide.repository.UserCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserCacheRepository redisRepository;



    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;


    public User loadUserByUserId(String userId) throws UsernameNotFoundException {
        return redisRepository.getUser(userId).orElseGet(
                () -> userRepository.findByUserId(userId).map(User::fromEntity).orElseThrow(
                        () -> new SafeGuideApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userId is %s", userId))
                ));
    }

    public String login(String userId, String password) {
        User savedUser = loadUserByUserId(userId);
        redisRepository.setUser(savedUser);
        if (!encoder.matches(password, savedUser.getPassword())) {
            throw new SafeGuideApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        return JwtTokenUtils.generateAccessToken(userId, secretKey, expiredTimeMs);
    }


    @Transactional
    public User join(String userId, String password, String userName,
                     String phoneNumber, String birth, String gender, String address) {
        userRepository.findByUserId(userId).ifPresent(it -> {
            throw new SafeGuideApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("userId is %s", userId));
        });

        UserEntity savedUser = userRepository.save(UserEntity.of(userId, encoder.encode(password),
                userName, phoneNumber, birth, gender, address));
        return User.fromEntity(savedUser);
    }

    @Transactional
    public Page<Alarm> alarmList(Integer idToken, Pageable pageable) {
        return alarmEntityRepository.findAllByUserUserId(idToken, pageable).map(Alarm::fromEntity);
    }

}
