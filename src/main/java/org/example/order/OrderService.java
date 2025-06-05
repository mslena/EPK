package org.example.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;

    public void insertOrder (Order order) {
        orderDAO.insertOrder(order);
    }

    public void updateOrder (Order order) {
        orderDAO.insertOrder(order);
    }

    public List<Order> getAllOrder () {
        return orderDAO.getAllOrder();
    }

    public List<Order> getOrder () {
        return orderDAO.getOrder();
    }

    public List<Order> getOrderByUserId (Long userId) {
        return orderDAO.getOrderByUserId(userId);
    }
}
