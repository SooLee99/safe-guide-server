package com.example.backend.safe_guide.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinRequest {
    private String userId;
    private String password;
    private String userName;
    private String phoneNumber;
    private String birth;
    private String gender;
    private String address;
}
