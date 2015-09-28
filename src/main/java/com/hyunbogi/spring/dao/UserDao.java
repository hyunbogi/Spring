package com.hyunbogi.spring.dao;

import com.hyunbogi.spring.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) throws SQLException {
        jdbcTemplate.update(
                "INSERT INTO users (id, name, password) VALUES (?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword()
        );
    }

    public User get(String id) throws SQLException {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                new Object[]{id},
                (rs, rowNum) -> {
                    return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
                }
        );
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("DELETE FROM users");
    }

    public int getCount() throws SQLException {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    }
}
