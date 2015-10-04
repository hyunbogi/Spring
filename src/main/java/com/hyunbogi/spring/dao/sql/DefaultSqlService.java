package com.hyunbogi.spring.dao.sql;

public class DefaultSqlService extends BaseSqlService {
    public DefaultSqlService() {
        sqlReader = new JaxbXmlSqlReader();
        sqlRegistry = new HashMapSqlRegistry();
    }
}
