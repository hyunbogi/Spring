package com.hyunbogi.spring.dao.sql;

import lombok.Setter;

import javax.annotation.PostConstruct;

public class BaseSqlService implements SqlService {
    @Setter
    protected SqlReader sqlReader;

    @Setter
    protected SqlRegistry sqlRegistry;

    @PostConstruct
    public void loadSql() {
        sqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }
}
