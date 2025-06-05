package org.example.product.typeProduct;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TypeProduct {
    @Override
    public String toString() {
        return name;
    }

    private final int id;
    private final String name;
}
