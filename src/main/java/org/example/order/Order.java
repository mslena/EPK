package org.example.order;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.product.Product;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class Order {
    private Long orderId;
    private Long userId;
    private String address;
    private List<Product> items;
    private String status;
    private String payment;
    private String note;
    private Date dateBegin;
    private String dateDelivery;
    private Date dateCor;

    public Order(Long userId, String address, String status, String payment, String note, Date dateBegin, String dateDelivery) {
        this.userId = userId;
        this.address = address;
        this.status = status;
        this.payment = payment;
        this.note = note;
        this.dateBegin = dateBegin;
        this.dateDelivery = dateDelivery;
    }

    public Order(Long orderId, String status, Date dateCor) {
        this.orderId = orderId;
        this.status = status;
        this.dateCor = dateCor;
    }
}
