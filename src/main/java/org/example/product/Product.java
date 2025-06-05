package org.example.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.product.typeProduct.TypeProduct;

@Getter
public class Product {
    private int id;
    private String name;
    private String articleNumber;
    private String manufacturer;
    private int quantity;
    private double price;
    private TypeProduct typeProduct;

    public Product(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Product(int id, String name, String articleNumber, String manufacturer, int quantity, double price, TypeProduct typeProduct) {
        this.id = id;
        this.name = name;
        this.articleNumber = articleNumber;
        this.manufacturer = manufacturer;
        this.quantity = quantity;
        this.price = price;
        this.typeProduct = typeProduct;
    }
}
