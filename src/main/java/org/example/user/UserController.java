package org.example.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.login.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {

    private final LoginService loginService;

    @GetMapping("/user")
    public ModelAndView user(HttpServletRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        UserAuth users = loginService.getUserByLogin(userId);
        String fullName = users.getFullName();;
        String firstName = fullName != null && fullName.contains(" ")
                ? fullName.split(" ")[0]
                : fullName;
        ModelAndView mav = new ModelAndView("user");
        mav.addObject("requestURI", request.getRequestURI());
        mav.addObject("users", users);
        mav.addObject("firstName", firstName);
        return mav;
    }

}
