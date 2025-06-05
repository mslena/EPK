package org.example.admin;

import lombok.Getter;
import org.example.product.Product;

import java.util.Date;
import java.util.List;

@Getter
public class Request {
    private Long orderId;
    private Long userId;
    private String address;
    private List<Product> items;

    public Request(Long orderId, Long userId, String address, List<Product> items, String fullName, String status, String payment, String note, Date dateBegin, String dateDelivery, Date dateCor) {
        this.orderId = orderId;
        this.userId = userId;
        this.address = address;
        this.items = items;
        this.fullName = fullName;
        this.status = status;
        this.payment = payment;
        this.note = note;
        this.dateBegin = dateBegin;
        this.dateDelivery = dateDelivery;
        this.dateCor = dateCor;
    }

    private String fullName;
    private String status;
    private String payment;
    private String note;
    private Date dateBegin;
    private String dateDelivery;
    private Date dateCor;

    public Request(Long orderId, Long userId, String address, List<Product> items, String status, String payment, String note, Date dateBegin, String dateDelivery, Date dateCor) {
        this.orderId = orderId;
        this.userId = userId;
        this.address = address;
        this.items = items;
        this.status = status;
        this.payment = payment;
        this.note = note;
        this.dateBegin = dateBegin;
        this.dateDelivery = dateDelivery;
        this.dateCor = dateCor;
    }
}
