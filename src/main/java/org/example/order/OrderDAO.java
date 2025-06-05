package org.example.order;

import org.example.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Lazy
@Repository("orderDao")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, readOnly = false)
public class OrderDAO extends NamedParameterJdbcDaoSupport {
    @Autowired
    void setJdbcTemplateVop(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Transactional
    public void insertOrder(Order order) {
        String sql = "insert into epk.\"order\" (user_id, status, address, payment, date_begin, date_delivery, note) values (:user_id, :status, :address, :payment, :date_begin, TO_DATE(:date_delivery, 'YYYY-MM-DD'), :note)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", order.getUserId());
        mapSqlParameterSource.addValue("status", order.getStatus());
        mapSqlParameterSource.addValue("address", order.getAddress());
        mapSqlParameterSource.addValue("payment", order.getPayment());
        mapSqlParameterSource.addValue("date_begin", order.getDateBegin());
        mapSqlParameterSource.addValue("date_delivery", order.getDateDelivery());
        mapSqlParameterSource.addValue("note", order.getNote());
        getNamedParameterJdbcTemplate().update(sql, mapSqlParameterSource);
    }

    public void updateStatus(Order order) {
        String sql = "update epk.\"order\" set date_cor = :date_cor,\n" +
                "status = :status where id_order = :order_id";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        for (Product item : order.getItems()) {
            mapSqlParameterSource.addValue("PRODUCT_ID", item.getId());
            mapSqlParameterSource.addValue("name", item.getName());
            mapSqlParameterSource.addValue("quantity", item.getQuantity());
            mapSqlParameterSource.addValue("price", item.getPrice());
        }



        int affectedRows = getNamedParameterJdbcTemplate().update(sql, mapSqlParameterSource);
        if (affectedRows != 1) {
            throw new RuntimeException("Ожидадось изменение 1 записи, а изменилось " + affectedRows + ". Операция отменена");
        }
    }


    public List<Order> getAllOrder() {
        return getJdbcTemplate().query(
                "Select * FROM epk.\"order\" as o\n" +
                "JOIN epk.\"Cart\" as car \n" +
                "ON o.id_order = car.order_id\n" +
                "join epk.\"Product\"  as prod\n" +
                "on car.product_id = prod.product_id" , new OrderResultSetExtractor());
    }

    public List<Order> getOrder() {
        return getJdbcTemplate().query(
                "Select * FROM epk.\"order\"\n" +
                        "\tORDER BY id_order DESC\n" +
                        "LIMIT 1;" , new OrderResultSetExtractor());
    }

    public List<Order> getOrderByUserId(Long userId) {
        String sql = "Select o.id_order, car.user_id, status, address, payment, date_cor, date_begin, date_delivery, note, car.product_id, name, car.quantity, price FROM epk.\"order\" as o \n" +
                "JOIN epk.\"Cart\" as car \n" +
                "ON o.id_order = car.order_id \n" +
                "join epk.\"Product\"  as prod \n" +
                "on car.product_id = prod.product_id \n" +
                "WHERE car.user_id = :user_id";

        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);
        return getNamedParameterJdbcTemplate().query(sql, params, new OrderResultSetExtractor());
    }


    private class OrderResultSetExtractor implements ResultSetExtractor<List<Order>> {

        @Override
        public List<Order> extractData(ResultSet rs) throws SQLException {
            Map<Long, Order> orderMap = new LinkedHashMap<>();
            Set<String> columnNames = getColumnNames(rs);

            while (rs.next()) {
                Long orderId = rs.getLong("id_order");

                Order order = orderMap.get(orderId);
                if (order == null) {
                    Long userId = rs.getLong("user_id");
                    String address = rs.getString("address");
                    String status = rs.getString("status");
                    String payment = rs.getString("payment");
                    String note = rs.getString("note");
                    Date dateBegin = rs.getDate("date_begin");
                    String dateDelivery = rs.getString("date_delivery");
                    Date dateCor = rs.getDate("date_cor");

                    order = new Order(orderId, userId, address, new ArrayList<>(), status, payment, note, dateBegin, dateDelivery, dateCor);
                    orderMap.put(orderId, order);
                }

                // Только если есть product_id и связанные поля, добавляем продукт
                if (columnNames.contains("product_id")) {
                    int productId = rs.getInt("product_id");
                    String name = columnNames.contains("name") ? rs.getString("name") : null;
                    int quantity = columnNames.contains("quantity") ? rs.getInt("quantity") : 0;
                    double price = columnNames.contains("price") ? rs.getDouble("price") : 0.0;

                    Product product = new Product(productId, name, quantity, price);
                    order.getItems().add(product);
                }
            }

            return new ArrayList<>(orderMap.values());
        }

        private Set<String> getColumnNames(ResultSet rs) throws SQLException {
            Set<String> columns = new HashSet<>();
            int columnCount = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(rs.getMetaData().getColumnName(i).toLowerCase());
            }
            return columns;
        }
    }




}
