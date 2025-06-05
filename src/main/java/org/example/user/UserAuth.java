package org.example.user;

import lombok.Getter;

import java.util.Objects;

@Getter
public class UserAuth {
    private Long userId;
    private  String login;
    private  String password;
    private  String fullName;
    private  String phoneNumber;
    private  String email;

    public UserAuth(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserAuth(String fullName, String phoneNumber, String email) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public UserAuth(Long userId, String login, String password, String fullName, String phoneNumber, String email) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public UserAuth(String login, String password, String fullName, String phoneNumber, String email) {
        this.login = login;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuth userAuth = (UserAuth) o;
        return Objects.equals(login, userAuth.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
