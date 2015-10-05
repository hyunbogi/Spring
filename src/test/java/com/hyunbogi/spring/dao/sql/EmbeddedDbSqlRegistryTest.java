package com.hyunbogi.spring.dao.sql;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    private EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:sql/sqldbSchema.sql")
                .addScript("classpath:sql/sqldbData.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void transactionalUpdate() {
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");

        // 예외를 발생시키기 위해 존재하지 않는 key를 사용하여 업데이트한다.
        sqlmap.put("unknown_key", "Modified_unknown");

        try {
            sqlRegistry.updateSql(sqlmap);
            fail("SqlUpdateFailureException expected");
        } catch (SqlUpdateFailureException e) {
            // do nothing
        }

        checkFindResult("SQL1", "SQL2", "SQL3");
    }
}
