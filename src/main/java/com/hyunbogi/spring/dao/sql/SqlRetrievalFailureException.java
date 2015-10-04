package com.hyunbogi.spring.dao.sql;

public class SqlRetrievalFailureException extends RuntimeException {
    public SqlRetrievalFailureException() {
    }

    public SqlRetrievalFailureException(String message) {
        super(message);
    }

    public SqlRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
