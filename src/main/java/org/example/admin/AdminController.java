package org.example.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    @GetMapping("/requests")
    public ModelAndView admin(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("requests");
        mav.addObject("requestURI", request.getRequestURI());
        return mav;
    }


}
