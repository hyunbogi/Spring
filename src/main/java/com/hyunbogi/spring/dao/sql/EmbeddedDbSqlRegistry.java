package com.hyunbogi.spring.dao.sql;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Map;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
    private JdbcTemplate jdbcTemplate;

    private TransactionTemplate transactionTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(dataSource)
        );
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        int affected = jdbcTemplate.update("UPDATE sqlmap SET sql_ = ? WHERE key_ = ?", sql, key);
        if (affected == 0) {
            throw new SqlUpdateFailureException("Cannot update SQL for: " + key);
        }
    }

    @Override
    public void updateSql(final Map<String, String> sqlmap) throws SqlUpdateFailureException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
                    updateSql(entry.getKey(), entry.getValue());
                }
            }
        });
    }

    @Override
    public void registerSql(String key, String sql) {
        jdbcTemplate.update("INSERT INTO sqlmap (key_, sql_) VALUES (?, ?)", key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try {
            return jdbcTemplate.queryForObject("SELECT sql_ FROM sqlmap WHERE key_ = ?", String.class, key);
        } catch (EmptyResultDataAccessException e) {
            throw new SqlNotFoundException("Cannot find SQL for: " + key, e);
        }
    }
}
