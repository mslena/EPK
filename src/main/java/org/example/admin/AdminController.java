package org.example.admin;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
public class AdminController {

    private final RequestService requestService;

    @GetMapping("/requests")
    public ModelAndView admin(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("requests");
        mav.addObject("request", requestService.getRequest());
        mav.addObject("requestURI", request.getRequestURI());
        return mav;
    }


}
