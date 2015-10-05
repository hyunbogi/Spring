package com.hyunbogi.spring.annotation;

import com.hyunbogi.spring.context.SqlServiceContext;
import org.springframework.context.annotation.Import;

@Import(SqlServiceContext.class)
public @interface EnableSqlService {
}
