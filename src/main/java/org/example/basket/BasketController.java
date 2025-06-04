package org.example.basket;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BasketController {
    @GetMapping("/basket")
    public ModelAndView basket(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("basket");
        mav.addObject("requestURI", request.getRequestURI());
        return mav;
    }

}
