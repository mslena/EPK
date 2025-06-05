package org.example.basket;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CartService {

    private final CartDAO cartDAO;
    private final CartUserDAO cartUserDAO;
    private final JdbcTemplate jdbcTemplate;


    public void addToCart(Long productId, int quantity, Long userId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM epk.\"Cart\" WHERE product_id = ?  and user_id = ? and active = false",
                Integer.class,
                productId, userId);

        if (count != null && count > 0) {
            jdbcTemplate.update(
                    "UPDATE epk.\"Cart\" SET quantity = quantity + ? WHERE product_id = ?  and user_id = ? and active = false",
                    quantity, productId, userId);
        } else {
            jdbcTemplate.update(
                    "INSERT INTO epk.\"Cart\" (user_id, product_id, quantity, active) VALUES (?, ?, ?, false)",
                    userId, productId, quantity);
        }
    }

    public void addFromCartUser(Long productId, Long userId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT quantity FROM epk.\"Cart\" WHERE product_id = ?  and user_id = ? and active = false",
                Integer.class,
                productId, userId);

        if (count != null) {
            jdbcTemplate.update(
                    "UPDATE epk.\"Cart\" SET quantity = quantity + 1 WHERE product_id = ? and user_id = ? and active = false",
                    productId, userId);
        }
    }

    public void updateQuantity(Long productId, int quantity, Long userId) {
        if (quantity <= 0) {
            removeFromCart(productId, userId);
        } else {
            jdbcTemplate.update(
                    "UPDATE epk.\"Cart\" SET quantity = ? WHERE product_id = ? and user_id = ? and active = false",
                    quantity, productId, userId);
        }

    }

    @Transactional
    public void removeFromCart(Long productId, Long userId) {
        jdbcTemplate.update(
                "delete from epk.\"Cart\"  WHERE product_id = ? and user_id = ? and active = false",
                productId, userId);
    }

    public void removeFromCartUser(Long productId, Long userId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT quantity FROM epk.\"Cart\" WHERE product_id = ? and user_id = ? and active = false",
                Integer.class,
                productId, userId);

        if (count != null && count > 1) {
            jdbcTemplate.update(
                    "UPDATE epk.\"Cart\" SET quantity = quantity - 1 WHERE product_id = ? and user_id = ? and active = false",
                     productId, userId);
        } else {
            jdbcTemplate.update(
                    "delete from epk.\"Cart\"  WHERE product_id = ? and user_id = ? and active = false",
                    productId, userId);
        }
    }

    public List<Cart> getCartItem(Long userId){
        return cartDAO.getAllCart(userId);
    }

    public List<CartUser> getCartUser(Long userId) {
        return cartUserDAO.getCartUser(userId);
    }

    public List<CartUser> getCartUsers(Long userId) {
        return cartUserDAO.getCartUsers(userId);
    }

    public void setOrder(Long userId, Long orderId){
        cartUserDAO.setOrder(userId, orderId);
    }
}
