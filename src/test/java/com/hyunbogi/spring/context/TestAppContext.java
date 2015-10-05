package com.hyunbogi.spring.context;

import com.hyunbogi.spring.mock.MockMailSender;
import com.hyunbogi.spring.service.UserService;
import com.hyunbogi.spring.service.UserServiceTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;

@Configuration
@Profile("test")
public class TestAppContext {
    @Bean
    public MailSender mailSender() {
        return new MockMailSender();
    }

    @Bean
    public UserService testUserService() {
        return new UserServiceTest.TestUserServiceImpl();
    }
}
