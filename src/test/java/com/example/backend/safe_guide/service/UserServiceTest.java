package com.example.backend.safe_guide.service;

import com.example.backend.safe_guide.exception.ErrorCode;
import com.example.backend.safe_guide.exception.SafeGuideApplicationException;
import com.example.backend.safe_guide.fixture.TestInfoFixture;
import com.example.backend.safe_guide.fixture.UserEntityFixture;
import com.example.backend.safe_guide.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserEntityRepository userEntityRepository;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void 로그인이_정상동작한다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userEntityRepository.findByUserId(fixture.getUserId())).thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> userService.login(fixture.getUserId(), fixture.getPassword()));

    }

    @Test
    void 로그인시_유저가_존재하지_않으면_에러를_내뱉는다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userEntityRepository.findByUserId(fixture.getUserId())).thenReturn(Optional.empty());
        SafeGuideApplicationException exception = Assertions.assertThrows(SafeGuideApplicationException.class
                , () -> userService.login(fixture.getUserId(), fixture.getPassword()));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void 로그인시_패스워드가_다르면_에러를_내뱉는다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userEntityRepository.findByUserId(fixture.getUserId())).thenReturn(Optional.of(UserEntityFixture.get(fixture.getUserId(), "password1")));
        when(bCryptPasswordEncoder.matches(fixture.getPassword(), "password1")).thenReturn(false);

        SafeGuideApplicationException exception = Assertions.assertThrows(SafeGuideApplicationException.class
                , () -> userService.login(fixture.getUserId(), fixture.getPassword()));

        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

    @Test
    void 회원가입이_정상동작한다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userEntityRepository.findByUserId(fixture.getUserId())).thenReturn(Optional.of(UserEntityFixture.get(fixture.getUserId(), fixture.getPassword())));
        when(bCryptPasswordEncoder.encode(fixture.getPassword())).thenReturn("password_encrypt");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(fixture.getUserId(), "password_encrypt")));

        Assertions.assertDoesNotThrow(() -> userService.join(fixture.getUserId(), fixture.getPassword(),
                fixture.getUserName(), fixture.getPhoneNumber(), fixture.getBirth(), fixture.getGender(), fixture.getAddress()));
    }


    @Test
    void 회원가입시_아이디가_중복되면_다르면_에러를_내뱉는다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(userEntityRepository.findByUserId(fixture.getUserId()))
                .thenReturn(Optional.of(UserEntityFixture.get(fixture.getUserId(), fixture.getPassword())));

        SafeGuideApplicationException exception = Assertions.assertThrows(SafeGuideApplicationException.class,
                () -> userService.join(fixture.getUserId(), fixture.getPassword(),
                        fixture.getUserName(), fixture.getPhoneNumber(), fixture.getBirth(), fixture.getGender(), fixture.getAddress()));

        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_ID, exception.getErrorCode());
    }

}
