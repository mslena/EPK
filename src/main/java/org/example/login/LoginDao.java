package org.example.login;

import org.example.product.Product;
import org.example.product.ProductDaoImpl;
import org.example.user.UserAuth;
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

@Repository
public class LoginDao extends NamedParameterJdbcDaoSupport {
    @Autowired
    void setJdbcTemplateVop(JdbcTemplate jdbcTemplate) {
        setJdbcTemplate(jdbcTemplate);
    }


    public List<UserAuth> getAllUser() {
        return getJdbcTemplate().query("SELECT * FROM epk.\"User\"", new UserResultSetExtractor());
    }

    public List<UserAuth> getUserByLogin(Long userId) {
        String sql = "SELECT * FROM epk.\"User\" WHERE user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource("user_id", userId);
        return getNamedParameterJdbcTemplate().query(sql, params, new UserRowMapper());
    }

    public void insertUser(UserAuth userAuth) {
        String sql = "insert into epk.\"User\" (login, password, full_name, phone_number, email) values (:login, :password, :full_name, :phone_number, :email)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("login", userAuth.getLogin());
        mapSqlParameterSource.addValue("password", userAuth.getPassword());
        mapSqlParameterSource.addValue("full_name", userAuth.getPassword());
        mapSqlParameterSource.addValue("phone_number", userAuth.getPassword());
        mapSqlParameterSource.addValue("email", userAuth.getPassword());
        getNamedParameterJdbcTemplate().update(sql, mapSqlParameterSource);
    }

    private class UserResultSetExtractor implements ResultSetExtractor<List<UserAuth>> {

        @Override
        public List<UserAuth> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<UserAuth> result = new ArrayList<>();
            LoginDao.UserRowMapper rowMapper = new LoginDao.UserRowMapper();
            while (rs.next()) {
                UserAuth user = rowMapper.mapRow(rs, 0);
                if (user != null) {
                    result.add(user);
                }
            }
            return result;
        }

    }

    private class UserRowMapper implements RowMapper<UserAuth> {
        @Override
        public UserAuth mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long userId = rs.getLong("user_id");
            String login = rs.getString("login");
            String password = rs.getString("password");
            String fullName = rs.getString("full_name");
            String phoneNumber = rs.getString("phone_number");
            String email = rs.getString("email");
            return new UserAuth(userId, login, password, fullName, phoneNumber, email);
        }
    }
}
