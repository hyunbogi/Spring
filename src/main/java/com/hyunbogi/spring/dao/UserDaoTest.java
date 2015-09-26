package com.hyunbogi.spring.dao;

import com.hyunbogi.spring.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("kmalloc");
        user.setName("Hyunbok Lee");
        user.setPassword("aaa12345");

        userDao.add(user);

        System.out.println(user.getId() + " success");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " success");
    }
}
