package org.example.product;

import org.example.product.typeProduct.TypeProduct;
import org.example.product.typeProduct.TypeProductService;
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
@Repository("productDao")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, readOnly = true)
public class ProductDaoImpl extends NamedParameterJdbcDaoSupport implements ProductDao{
    private final TypeProductService typeProductService;

    @Autowired
    public ProductDaoImpl(TypeProductService typeProductService) {
        this.typeProductService = typeProductService;
    }

    @Autowired
    void setJdbcTemplateVop(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void insertProduct(Product product) {
        String sql = "insert into epk.\"Product\" (name, article_number, manufacturer, quantity, price, type_product_id) values (:name, :article_number, :manufacturer, :quantity, :price, :type_product_id)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", product.getName());
        mapSqlParameterSource.addValue("article_number", product.getArticleNumber());
        mapSqlParameterSource.addValue("manufacturer", product.getManufacturer());
        mapSqlParameterSource.addValue("quantity", product.getQuantity());
        mapSqlParameterSource.addValue("price", product.getPrice());
        mapSqlParameterSource.addValue("type_product_id", product.getTypeProduct().getId());
        getNamedParameterJdbcTemplate().update(sql, mapSqlParameterSource);
    }

    @Override
    public void updateProduct(Product product) {
        String sql = "update epk.\"Product\" set name = :name,\n" +
                    "article_number = :article_number,\n" +
                    "manufacturer = :manufacturer,\n" +
                    "quantity = :quantity,\n" +
                    "price = :price,\n" +
                    "type_product_id = :type_product_id where PRODUCT_ID = :PRODUCT_ID";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("PRODUCT_ID", product.getId());
        mapSqlParameterSource.addValue("name", product.getName());
        mapSqlParameterSource.addValue("article_number", product.getArticleNumber());
        mapSqlParameterSource.addValue("manufacturer", product.getManufacturer());
        mapSqlParameterSource.addValue("quantity", product.getQuantity());
        mapSqlParameterSource.addValue("price", product.getPrice());
        mapSqlParameterSource.addValue("type_product_id", product.getTypeProduct().getId());

        int affectedRows = getNamedParameterJdbcTemplate().update(sql, mapSqlParameterSource);
        if (affectedRows != 1) {
            throw new RuntimeException("Ожидадось изменение 1 записи, а изменилось " + affectedRows + ". Операция отменена");
        }
    }

    @Override
    public void deleteProduct(Product product) {
        if(product == null) {
            throw new RuntimeException("Новость не должна быть null");
        }
        String sql = "delete from epk.\"Product\" where PRODUCT_ID = :PRODUCT_ID";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("PRODUCT_ID", product.getId());
        int affectedRows = getNamedParameterJdbcTemplate().update(sql, mapSqlParameterSource);
        if (affectedRows != 1) {
            throw new RuntimeException("Ожидадось удаление 1 записи, а удалилось " + affectedRows + ". Операция отменена");
        }
    }

    @Override
    public List<Product> getAllProduct() {
        return getJdbcTemplate().query("select * from epk.\"Product\"", new ProductResultSetExtractor());
    }

    @Override
    public List<Product> getProductByArticle(String articleNumber) {
        String sql = "SELECT * FROM epk.\"Product\" WHERE ARTICLE_NUMBER = :ARTICLE_NUMBER";
        MapSqlParameterSource params = new MapSqlParameterSource("ARTICLE_NUMBER", articleNumber);
        return getNamedParameterJdbcTemplate().query(sql, params, new ProductRowMapper());
    }

    @Override
    public List<Product> getProductByManufacturer(String manufacturer) {
        return null;
    }

    private class ProductResultSetExtractor implements ResultSetExtractor<List<Product>> {

        @Override
        public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Product> result = new ArrayList<>();
            ProductRowMapper rowMapper = new ProductRowMapper();
            while (rs.next()) {
                Product product = rowMapper.mapRow(rs, 0);
                if (product != null) {
                    result.add(product);
                }
            }
            return result;
        }

    }

    private class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            TypeProduct typeProduct;
            int productId = rs.getInt("PRODUCT_ID");
            int typeProductId = rs.getInt("TYPE_PRODUCT_ID");
            if(typeProductId != 0){
                typeProduct = typeProductService.getTypeProductById(typeProductId);
            }else {
                typeProduct = null;
            }
            String name = rs.getString("NAME");
            String articleNumber = rs.getString("ARTICLE_NUMBER");
            String manufacturer = rs.getString("MANUFACTURER");
            int quantity = rs.getInt("QUANTITY");
            double price = rs.getDouble("PRICE");
            return new Product(productId, name, articleNumber, manufacturer, quantity, price, typeProduct);
        }
    }
}
