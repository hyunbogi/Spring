package com.hyunbogi.spring.service;

import com.hyunbogi.spring.dao.UserDao;
import com.hyunbogi.spring.model.Level;
import com.hyunbogi.spring.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private MailSender mailSender;

    private List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("aaa", "Jimin Han", "aaa12345",
                        Level.BASIC, UserService.MIN_LOGIN_FOR_SILVER - 1, 0, "jmh@email.com"),
                new User("bbb", "Choa", "aaa12345",
                        Level.BASIC, UserService.MIN_LOGIN_FOR_SILVER, 0, "choa@email.com"),
                new User("ccc", "Ailee", "aaa12345",
                        Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD - 1, "ailee@email.com"),
                new User("ddd", "Seulgi Kim", "aaa12345",
                        Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD, "sgk@email.com"),
                new User("eee", "Hyeri", "aaa12345",
                        Level.GOLD, 100, Integer.MAX_VALUE, "hyeri@email.com")
        );
    }

    @Test
    public void upgradeLevels() throws Exception {
        userDao.deleteAll();
        users.forEach(userDao::add);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setTransactionManager(transactionManager);
        testUserService.setMailSender(mailSender);

        userDao.deleteAll();
        users.forEach(userDao::add);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
            // do nothing
        }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    private static class TestUserService extends UserService {
        private String id;

        public TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(id)) {
                throw new TestUserServiceException();
            }

            super.upgradeLevel(user);
        }
    }

    private static class TestUserServiceException extends RuntimeException {
        // empty
    }
}
