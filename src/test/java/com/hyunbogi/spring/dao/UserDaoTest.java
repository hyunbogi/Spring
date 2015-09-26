package com.hyunbogi.spring.dao;

import com.hyunbogi.spring.model.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {
    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
        User user1 = new User();
        user1.setId("kmalloc");
        user1.setName("Hyunbok Lee");
        user1.setPassword("aaa12345");

        dao.add(user1);

        User user2 = dao.get(user1.getId());

        assertThat(user2.getName(), is(user1.getName()));
        assertThat(user2.getPassword(), is(user1.getPassword()));
    }
}
