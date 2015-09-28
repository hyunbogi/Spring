package com.hyunbogi.spring.dao;

import com.hyunbogi.spring.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> rowMapper = (rs, rowNum) ->
            new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (id, name, password) VALUES (?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword()
        );
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                new Object[]{id},
                rowMapper
        );
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users ORDER BY id",
                rowMapper
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    }
}
