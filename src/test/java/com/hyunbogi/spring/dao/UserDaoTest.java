package com.hyunbogi.spring.dao;

import com.hyunbogi.spring.model.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {
    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
        User user1 = new User("kmalloc", "Hyunbok Lee", "aaa12345");
        User user2 = new User("iu", "Jieun Lee", "aaa12345");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userGet1 = dao.get(user1.getId());
        assertThat(userGet1.getName(), is(user1.getName()));
        assertThat(userGet1.getPassword(), is(user1.getPassword()));

        User userGet2 = dao.get(user2.getId());
        assertThat(userGet2.getName(), is(user2.getName()));
        assertThat(userGet2.getPassword(), is(user2.getPassword()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }

    @Test
    public void count() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
        User user1 = new User("tang", "Taeyeon Kim", "aaa12345");
        User user2 = new User("iu", "Jieun Lee", "aaa12345");
        User user3 = new User("bong", "Boyoung Park", "aaa12345");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }
}
