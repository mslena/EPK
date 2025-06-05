package org.example.basket;

import org.example.product.ProductDaoImpl;
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
@Repository("cartDao")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
public class CartDAO extends NamedParameterJdbcDaoSupport {
    @Autowired
    void setJdbcTemplateVop(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional
    public void deleteCart(Long cartId, Long userID) {
        if(cartId == null) {
            throw new RuntimeException("product_id null");
        }
        String sql = "delete from epk.\"Cart\" where cart_id = :cart_id and user_id = :user_id  and active = false";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("cart_id", cartId);
        mapSqlParameterSource.addValue("user_id", userID);
        int affectedRows = getNamedParameterJdbcTemplate().update(sql, mapSqlParameterSource);
        if (affectedRows != 1) {
            throw new RuntimeException("Ожидадось удаление 1 записи, а удалилось " + affectedRows + ". Операция отменена");
        }
    }

    public List<Cart> getAllCart(Long userId) {
        String sql = "select * from epk.\"Cart\" where user_id = :user_id and active = false";
        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);
        return getNamedParameterJdbcTemplate().query(sql, params, new CartRowMapper());
    }
    private class CartResultSetExtractor implements ResultSetExtractor<List<Cart>> {

        @Override
        public List<Cart> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Cart> result = new ArrayList<>();
            CartRowMapper rowMapper = new CartRowMapper();
            while (rs.next()) {
                Cart cart = rowMapper.mapRow(rs, 0);
                if (cart != null) {
                    result.add(cart);
                }
            }
            return result;
        }

    }



    private class CartRowMapper implements RowMapper<Cart> {
        @Override
        public Cart mapRow(ResultSet rs, int rowNum) throws SQLException {
            int cartId = rs.getInt("Cart_ID");
            Long productId = rs.getLong("PRODUCT_ID");
            int quantity = rs.getInt("QUANTITY");
            return new Cart(cartId,productId, quantity);
        }
    }
}
