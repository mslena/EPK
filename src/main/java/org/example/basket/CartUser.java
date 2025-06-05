package org.example.basket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartUser {
    private Long productId;
    private final String name;
    private final int quantity;
    private final double price;


}
