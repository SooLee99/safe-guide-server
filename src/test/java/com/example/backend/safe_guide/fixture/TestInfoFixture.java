package com.example.backend.safe_guide.fixture;

import lombok.Data;

public class TestInfoFixture {

    public static TestInfo get() {
        TestInfo info = new TestInfo();
        info.setPostId(1);
        info.setIdToken(1);
        info.setUserId("name");
        info.setPassword("password");
        info.setUserName("userName");
        info.setPhoneNumber("phoneNumber");
        info.setBirth("birth");
        info.setGender("gender");
        info.setAddress("address");
        info.setTitle("title");
        info.setBody("body");
        return info;
    }

    @Data
    public static class TestInfo {
        private Integer postId;
        private Integer idToken;
        private String userId;
        private String password;
        private String userName;
        private String phoneNumber;
        private String birth;
        private String gender;
        private String address;
        private String title;
        private String body;
    }
}

