package com.example.backend.safe_guide.controller.response;

import com.example.backend.safe_guide.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public
class UserResponse {
    private Integer idToken;
    private String userId;

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getIdToken(),
                user.getUserId()
        );
    }

}
