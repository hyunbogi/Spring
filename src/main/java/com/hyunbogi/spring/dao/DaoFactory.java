package com.hyunbogi.spring.dao;

import com.hyunbogi.spring.db.ConnectionMaker;
import com.hyunbogi.spring.db.DConnectionMaker;

public class DaoFactory {
    public UserDao userDao() {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        return new UserDao(connectionMaker);
    }
}
