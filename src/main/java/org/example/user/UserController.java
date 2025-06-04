package org.example.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @GetMapping("/user")
    public ModelAndView user(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("user");
        mav.addObject("requestURI", request.getRequestURI());
        return mav;
    }

}
