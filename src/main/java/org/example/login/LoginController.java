package org.example.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.user.UserAuth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final LoginDao loginDao;

    @GetMapping("/login")
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("requestURI", request.getRequestURI());
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("login") String login,
                              @RequestParam("password") String password,
                              HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();

        if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
            modelAndView.setViewName("login");
            modelAndView.addObject("error", "Поля не должны быть пустыми");
            return modelAndView;
        }else {
            List<UserAuth> users = loginDao.getAllUser();

            if ("admin".equals(login) && "admin".equals(password)) {
                modelAndView.setViewName("redirect:/requests");
                return modelAndView;
            } else {
                for (UserAuth user : users) {
                    if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                        modelAndView.setViewName("redirect:/user");
                        session.setAttribute("userId", user.getUserId());
                        return modelAndView;
                    }
                }

            }

            return modelAndView;
        }

    }

    @GetMapping("/registration")
    public ModelAndView registration() {
        return new ModelAndView("registration");
    }

    @PostMapping("/registration")
    public ModelAndView processRegistration(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      @RequestParam String surname, @RequestParam String name,
                                      @RequestParam String patronymic, @RequestParam String phoneNumber,
                                      @RequestParam String email,
                                                        RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (password.equals(confirmPassword)) {
            UserAuth userAuth = new UserAuth(username, password, surname + name + patronymic, phoneNumber, email);
            String message = loginService.insertUser(userAuth);
            redirectAttributes.addFlashAttribute("message", message);
            modelAndView.setViewName("redirect:/login");
        }
        else {
            modelAndView.setViewName("registration");
            modelAndView.addObject("error", "Пароли должны совпадать");
            modelAndView.addObject("username", username);
        }

        return modelAndView;
    }

}
