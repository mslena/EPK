package org.example.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuth {
    private final String login;
    private final String password;
}
