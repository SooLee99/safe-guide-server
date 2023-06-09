package com.example.backend.safe_guide.controller.response;

import com.example.backend.safe_guide.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public
class UserJoinResponse {
    private Integer idToken;
    private String userId;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getIdToken(),
                user.getUserId()
        );
    }

}
