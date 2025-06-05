package org.example.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.order.Order;
import org.example.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
@AllArgsConstructor
public class AdminController {

    private final RequestService requestService;
    private final OrderService orderService;

    @GetMapping("/requests")
    public ModelAndView admin(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("requests");
        mav.addObject("request", requestService.getRequest());
        mav.addObject("requestURI", request.getRequestURI());
        return mav;
    }

    @PostMapping("/request/add/{orderId}")
    public ModelAndView removeFromCart(@PathVariable("orderId") Long orderId) {
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Order order = new Order(orderId, "Принят в работу",date);
        orderService.updateOrder(order); // удаление из корзины
        return new ModelAndView("redirect:/requests");
    }

    @PostMapping("/request/remove/{orderId}")
    public ModelAndView addFromCart(@PathVariable("orderId") Long orderId) {
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Order order = new Order(orderId, "Отклонен",date);
        orderService.updateOrder(order); // удаление из корзины
        return new ModelAndView("redirect:/requests");
    }
}
