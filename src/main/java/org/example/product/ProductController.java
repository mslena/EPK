package org.example.product;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.basket.Cart;
import org.example.basket.CartRequest;
import org.example.basket.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/adminProduct")
    public ModelAndView adminProduct(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("adminProduct").addObject("product" ,productService.getAllProduct());
        mav.addObject("requestURI", request.getRequestURI());
        return mav;
    }

    @GetMapping("/product")
    public ModelAndView product(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("product").addObject("product" ,productService.getAllProduct());
        mav.addObject("requestURI", request.getRequestURI());
        return mav;
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(@RequestBody CartRequest request,
                                       HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        cartService.addToCart(request.getProductId(), request.getQuantity(), userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart/update")
    public ResponseEntity<?> updateQuantity(@RequestBody CartRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        cartService.updateQuantity(request.getProductId(), request.getQuantity(), userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart/remove")
    public ResponseEntity<?> removeFromCart(@RequestBody CartRequest request,
                                            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        cartService.removeFromCart(request.getProductId(), userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cart/items")
    public ResponseEntity<List<Cart>> getCartItems(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return ResponseEntity.ok(cartService.getCartItem(userId));
    }

}
