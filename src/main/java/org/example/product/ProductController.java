package org.example.product;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

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
}
