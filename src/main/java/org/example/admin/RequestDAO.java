package org.example.admin;

import org.example.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Lazy
@Repository("requestDao")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, readOnly = false)
public class RequestDAO extends NamedParameterJdbcDaoSupport {

    @Autowired
    void setJdbcTemplateVop(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }


    public List<Request> getAllRequest() {
        return getJdbcTemplate().query(
                "Select order_id, car.user_id, full_name, address, status, payment, note, date_begin, date_delivery, date_cor, car.product_id, name, car.quantity, price FROM epk.\"order\" as o\n" +
                        "JOIN epk.\"Cart\" as car\n" +
                        "ON o.id_order = car.order_id\n" +
                        "join epk.\"Product\"  as prod\n" +
                        "on car.product_id = prod.product_id\n" +
                        "join epk.\"User\" as users\n" +
                        "on car.user_id = users.user_id" , new RequestResultSetExtractor());
    }


    private class RequestResultSetExtractor implements ResultSetExtractor<List<Request>> {

        @Override
        public List<Request> extractData(ResultSet rs) throws SQLException {
            Map<Long, Request> orderMap = new LinkedHashMap<>();
            Set<String> columnNames = getColumnNames(rs);

            while (rs.next()) {
                Long orderId = rs.getLong("order_id");

                Request order = orderMap.get(orderId);
                if (order == null) {
                    Long userId = rs.getLong("user_id");
                    String fullName = rs.getString("full_name");
                    String address = rs.getString("address");
                    String status = rs.getString("status");
                    String payment = rs.getString("payment");
                    String note = rs.getString("note");
                    Date dateBegin = rs.getDate("date_begin");
                    String dateDelivery = rs.getString("date_delivery");
                    Date dateCor = rs.getDate("date_cor");

                    order = new Request(orderId, userId, address, new ArrayList<>(), fullName, status, payment, note, dateBegin, dateDelivery, dateCor);
                    orderMap.put(orderId, order);
                }

                // Только если есть product_id и связанные поля, добавляем продукт
                if (columnNames.contains("product_id")) {
                    int productId = rs.getInt("product_id");
                    Long userId = rs.getLong("user_id");
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
