package org.example.basket;

import org.example.login.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Lazy
@Repository("cartUserDao")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, readOnly = false)
public class CartUserDAO  extends NamedParameterJdbcDaoSupport {
    @Autowired
    void setJdbcTemplateVop(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    public List<CartUser> getCartUser(Long userId){
        String sql = "Select cart.product_id, name, price, cart.quantity from epk.\"Cart\" as cart\n" +
                "join epk.\"Product\"  as prod\n" +
                "on cart.product_id = prod.product_id\n" +
                "where cart.user_id = :user_id and cart.active = false";
        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);
        return getNamedParameterJdbcTemplate().query(sql, params, new CartUserRowMapper());
    }

    public List<CartUser> getCartUsers(Long userId){
        String sql = "Select cart.product_id, name, price, cart.quantity from epk.\"Cart\" as cart\n" +
                "join epk.\"Product\"  as prod\n" +
                "on cart.product_id = prod.product_id\n" +
                "where cart.user_id = :user_id and cart.active = true";
        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);
        return getNamedParameterJdbcTemplate().query(sql, params, new CartUserRowMapper());
    }

    public void setOrder(Long userId, Long orderId) {
        String sql = "UPDATE epk.\"Cart\"\n" +
                "\tSET active=true, order_id=:order_id\n" +
                "\tWHERE  user_id=:user_id and order_id IS NULL;";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("order_id", orderId);
        params.addValue("user_id", userId);

        int affectedRows = getNamedParameterJdbcTemplate().update(sql, params);

        // (опционально) лог или проверка
        if (affectedRows == 0) {
            throw new RuntimeException("Не удалось обновить записи в корзине для user_id: " + userId);
        }
    }


    private class CartUserResultSetExtractor implements ResultSetExtractor<List<CartUser>> {

        @Override
        public List<CartUser> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<CartUser> result = new ArrayList<>();
            CartUserRowMapper rowMapper = new CartUserRowMapper();
            while (rs.next()) {
                CartUser cartUser = rowMapper.mapRow(rs, 0);
                if (cartUser != null) {
                    result.add(cartUser);
                }
            }
            return result;
        }

    }

    private class CartUserRowMapper implements RowMapper<CartUser> {
        @Override
        public CartUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long productId = rs.getLong("PRODUCT_ID");
            String name = rs.getString("NAME");
            int quantity = rs.getInt("QUANTITY");
            double price = rs.getDouble("PRICE");
            return new CartUser(productId, name, quantity, price);
        }
    }
}
