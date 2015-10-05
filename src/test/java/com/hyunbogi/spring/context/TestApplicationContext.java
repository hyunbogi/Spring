package com.hyunbogi.spring.context;

import com.hyunbogi.spring.dao.UserDao;
import com.hyunbogi.spring.dao.UserDaoJdbc;
import com.hyunbogi.spring.dao.sql.EmbeddedDbSqlRegistry;
import com.hyunbogi.spring.dao.sql.OxmSqlService;
import com.hyunbogi.spring.dao.sql.SqlRegistry;
import com.hyunbogi.spring.dao.sql.SqlService;
import com.hyunbogi.spring.mock.MockMailSender;
import com.hyunbogi.spring.service.UserService;
import com.hyunbogi.spring.service.UserServiceImpl;
import com.hyunbogi.spring.service.UserServiceTest;
import com.mysql.jdbc.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class TestApplicationContext {
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/springtestdb");
        dataSource.setUsername("hyunbogi");
        dataSource.setPassword("aaa12345");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());

        return tm;
    }

    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        userDao.setSqlService(sqlService());

        return userDao;
    }

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());

        return sqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());

        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setContextPath("com.hyunbogi.spring.dao.sql.jaxb");

        return unmarshaller;
    }

    @Bean
    public MailSender mailSender() {
        return new MockMailSender();
    }

    @Bean
    public UserService userService() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        userService.setMailSender(mailSender());

        return userService;
    }

    @Bean
    public UserService testUserService() {
        UserServiceTest.TestUserServiceImpl testUserService = new UserServiceTest.TestUserServiceImpl();
        testUserService.setUserDao(userDao());
        testUserService.setMailSender(mailSender());

        return testUserService;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:sql/sqlRegistrySchema.sql")
                .build();
    }
}
