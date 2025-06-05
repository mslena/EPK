package org.example.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Cart {
    private int cartId;
    private Long productId;
    private int quantity;

    public Cart() {
    }

    public Cart(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}