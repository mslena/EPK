package org.example.order;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.basket.CartService;
import org.example.basket.CartUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/order")
    public ModelAndView user(HttpServletRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        List<CartUser> cart = cartService.getCartUsers(userId);
        double total = cart.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        ModelAndView mav = new ModelAndView("order");
        mav.addObject("order", orderService.getOrderByUserId(userId));
        mav.addObject("requestURI", request.getRequestURI());

        return mav;
    }


}
