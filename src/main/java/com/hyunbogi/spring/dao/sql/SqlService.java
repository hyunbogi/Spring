package com.hyunbogi.spring.dao.sql;

public interface SqlService {
    /**
     * key 값에 해당하는 SQL문을 가져온다.
     *
     * @param key SQL에 대한 key
     * @return SQL
     * @throws SqlRetrievalFailureException
     */
    String getSql(String key) throws SqlRetrievalFailureException;
}
