package com.hyunbogi.spring.dao;

import com.hyunbogi.spring.model.Level;
import com.hyunbogi.spring.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));

        return user;
    };


    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (id, name, password, level, login, recommend) VALUES (?, ?, ?, ?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend()
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
    public void update(User user) {
        jdbcTemplate.update(
                "UPDATE users SET name = ?, password = ?, level = ?, login = ?, recommend = ? WHERE id = ?",
                user.getName(),
                user.getPassword(),
                user.getLevel().intValue(),
                user.getLogin(),
                user.getRecommend(),
                user.getId()
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
