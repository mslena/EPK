package org.example.login;

import lombok.AllArgsConstructor;
import org.example.user.UserAuth;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoginService {

    private final LoginDao loginDao;

    public UserAuth getUserByLogin(Long userId) {
        List<UserAuth> userAuths = loginDao.getUserByLogin(userId);
        return userAuths.get(0);
    }

    public String insertUser(UserAuth user) {
        if(user != null) {
            loginDao.insertUser(user);
            return "Вы успешно зарегистрированы!";
        } else {
            return "Ошибка регистрации!";
        }
    }

}
