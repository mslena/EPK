package org.example.product.typeProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository("typeProductDao")
public class TypeProductDaoImpl extends NamedParameterJdbcDaoSupport implements TypeProductDao{

    @Autowired
    void setJdbcTemplateVop(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void insertTypeProduct(TypeProduct typeProduct) {
        String sql = "insert into epk.\"TypeProduct\" (NAME) values ( :NAME)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("NAME", typeProduct.getName());
        getNamedParameterJdbcTemplate().update(sql, mapSqlParameterSource);
    }

    @Override
    public void updateTypeProduct(TypeProduct typeProduct) {
        String sql = "update epk.\"TypeProduct\" set NAME = :NAME where TYPE_PRODUCT_ID = :TYPE_PRODUCT_ID";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("TYPE_PRODUCT_ID", typeProduct.getId());
        mapSqlParameterSource.addValue("NAME", typeProduct.getName());
        int affectedRows = getNamedParameterJdbcTemplate().update(sql, mapSqlParameterSource);
        if (affectedRows != 1) {
            throw new RuntimeException("Ожидадось изменение 1 записи, а изменилось " + affectedRows + ". Операция отменена");
        }
    }

    @Override
    public void deleteTypeProduct(TypeProduct typeProduct) {
        if(typeProduct == null) {
            throw new RuntimeException("Новость не должна быть null");
        }
        String sql = "delete from epk.\"TypeProduct\" where TYPE_PRODUCT_ID = :TYPE_PRODUCT_ID";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("TYPE_PRODUCT_ID", typeProduct.getId());
        int affectedRows = getNamedParameterJdbcTemplate().update(sql, mapSqlParameterSource);
        if (affectedRows != 1) {
            throw new RuntimeException("Ожидадось удаление 1 записи, а удалилось " + affectedRows + ". Операция отменена");
        }
    }

    @Override
    public List<TypeProduct> getAllTypeProduct() {
        return getJdbcTemplate().query("select * from epk.\"TypeProduct\"", new TypeProductResultSetExtractor());
    }

    @Override
    public TypeProduct findById(int id){
        String sql = "SELECT * FROM epk.\"TypeProduct\" WHERE TYPE_PRODUCT_ID = :TYPE_PRODUCT_ID";
        MapSqlParameterSource params = new MapSqlParameterSource("TYPE_PRODUCT_ID", id);
        List<TypeProduct> result = getNamedParameterJdbcTemplate().query(sql, params, new TypeProductRowMapper());
        return result.isEmpty() ? null : result.get(0);
    }
    private class TypeProductResultSetExtractor implements ResultSetExtractor<List<TypeProduct>> {

        @Override
        public List<TypeProduct> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<TypeProduct> result = new ArrayList<>();
            TypeProductRowMapper rowMapper = new TypeProductRowMapper();
            while (rs.next()) {
                TypeProduct typeProduct = rowMapper.mapRow(rs, 0);
                if (typeProduct != null) {
                    result.add(typeProduct);
                }
            }
            return result;
        }

    }

    private class TypeProductRowMapper implements RowMapper<TypeProduct> {
        @Override
        public TypeProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
            int  typeProductId = rs.getInt("TYPE_PRODUCT_ID");
            String name = rs.getString("NAME");
            return new TypeProduct(typeProductId, name);
        }
    }
}
