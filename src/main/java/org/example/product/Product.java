package org.example.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.product.typeProduct.TypeProduct;

@Getter
@AllArgsConstructor
public class Product {
    private final Long id;
    private final String name;
    private final String articleNumber;
    private final String manufacturer;
    private final int quantity;
    private final double price;
    private final TypeProduct typeProduct;
}
