package com.hyunbogi.spring.dao.sql;

public class SqlNotFoundException extends RuntimeException {
    public SqlNotFoundException() {
    }

    public SqlNotFoundException(String message) {
        super(message);
    }

    public SqlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
