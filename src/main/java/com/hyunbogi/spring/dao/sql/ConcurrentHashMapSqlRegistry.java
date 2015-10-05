package com.hyunbogi.spring.dao.sql;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {
    private Map<String, String> concurrentSqlMap = new ConcurrentHashMap<>();

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        if (concurrentSqlMap.get(key) == null) {
            throw new SqlUpdateFailureException("Cannot update SQL for: " + key);
        }

        concurrentSqlMap.put(key, sql);
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        concurrentSqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = concurrentSqlMap.get(key);
        if (sql == null) {
            throw new SqlNotFoundException("Cannot find SQL for: " + key);
        }

        return sql;
    }
}
