package org.example.login;

import lombok.AllArgsConstructor;
import org.example.user.UserAuth;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoginService {

    private final LoginDao loginDao;

    public String getLogin(String login, String password) {
        List<UserAuth> users = loginDao.getAllUser();

        if ("admin".equals(login) && "admin".equals(password)) {
            return "redirect:/requests";
        } else {
            for (UserAuth user : users) {
                if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                    return "redirect:/user";
                }
            }

        }

        return "login";
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
