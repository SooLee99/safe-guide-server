    package com.example.backend.safe_guide.exception;

    import lombok.Getter;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;

    @Getter
    @RequiredArgsConstructor
    public enum ErrorCode {

        INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 정보입니다."),
        USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
        POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."),
        INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "유효하지 않은 비밀번호입니다."),
        DUPLICATED_USER_ID(HttpStatus.CONFLICT, "중복된 사용자 아이디입니다."),
        ALREADY_LIKED_POST(HttpStatus.CONFLICT, "사용자가 이미 게시물을 좋아합니다."),
        INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자에게 유효하지 않은 권한이 있습니다."),
        DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
        NOTIFICATION_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알림 서버에 연결 오류가 발생했습니다."),
        ;

        private final HttpStatus status;
        private final String message;
    }
