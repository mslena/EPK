package org.example.basket;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.order.Order;
import org.example.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@AllArgsConstructor
public class CartController {
    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/cart")
    public ModelAndView basket(HttpServletRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        List<CartUser> cart = cartService.getCartUser(userId);
        double total = cart.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        ModelAndView mav = new ModelAndView("cart");
        mav.addObject("requestURI", request.getRequestURI());
        mav.addObject("cart", cartService.getCartUser(userId));
        mav.addObject("total", total);
        return mav;
    }

    @PostMapping("/cart/remove/{productId}")
    public ModelAndView removeFromCart(@PathVariable("productId") Long productId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        cartService.removeFromCartUser(productId, userId); // удаление из корзины
        return new ModelAndView("redirect:/cart"); // перенаправление обратно на корзину
    }

    @PostMapping("/cart/add/{productId}")
    public ModelAndView addFromCart(@PathVariable("productId") Long productId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        cartService.addFromCartUser(productId, userId); // удаление из корзины
        return new ModelAndView("redirect:/cart"); // перенаправление обратно на корзину
    }

    @PostMapping("/cart")
    public ModelAndView addOrder(@RequestParam String address, @RequestParam String dateDelivery,
                                 @RequestParam String payment, @RequestParam String note, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Long userId = (Long) session.getAttribute("userId");
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Order order = new Order(userId, address, "В обработке", payment, note, date, dateDelivery);
        orderService.insertOrder(order);
        List<Order> orderList = orderService.getOrder();
        cartService.setOrder(userId, orderList.get(0).getOrderId());
        modelAndView.setViewName("redirect:/order");
        return modelAndView;
    }
}
