package org.example.restlogisticserp.models;

public class UserAuthResponse {
    private User user;
    private UserAuth userAuth;

    public UserAuthResponse(User user, UserAuth userAuth) {
        this.user = user;
        this.userAuth = userAuth;
    }

    public User getUser() {
        return user;
    }

    public UserAuth getUserAuth() {
        return userAuth;
    }
}
