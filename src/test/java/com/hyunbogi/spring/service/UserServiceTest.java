package com.hyunbogi.spring.service;

import com.hyunbogi.spring.context.AppContext;
import com.hyunbogi.spring.context.TestAppContext;
import com.hyunbogi.spring.dao.UserDao;
import com.hyunbogi.spring.mock.MockMailSender;
import com.hyunbogi.spring.model.Level;
import com.hyunbogi.spring.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {AppContext.class, TestAppContext.class})
public class UserServiceTest {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("testUserService")
    private UserService testUserService;

    @Autowired
    private UserDao userDao;

    private List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("aaa", "Jimin Han", "aaa12345",
                        Level.BASIC, UserServiceImpl.MIN_LOGIN_FOR_SILVER - 1, 0, "jmh@email.com"),
                new User("bbb", "Choa", "aaa12345",
                        Level.BASIC, UserServiceImpl.MIN_LOGIN_FOR_SILVER, 0, "choa@email.com"),
                new User("ccc", "Ailee", "aaa12345",
                        Level.SILVER, 60, UserServiceImpl.MIN_RECOMMEND_FOR_GOLD - 1, "ailee@email.com"),
                new User("ddd", "Seulgi Kim", "aaa12345",
                        Level.SILVER, 60, UserServiceImpl.MIN_RECOMMEND_FOR_GOLD, "sgk@email.com"),
                new User("eee", "Hyeri", "aaa12345",
                        Level.GOLD, 100, Integer.MAX_VALUE, "hyeri@email.com")
        );
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }

    @Test
    public void upgradeLevels() throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(users);
        MockMailSender mockMailSender = new MockMailSender();

        userServiceImpl.setUserDao(mockUserDao);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0), "bbb", Level.SILVER);
        checkUserAndLevel(updated.get(1), "ddd", Level.GOLD);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests.size(), is(2));
        assertThat(requests.get(0), is(users.get(1).getEmail()));
        assertThat(requests.get(1), is(users.get(3).getEmail()));
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
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

    @Test(expected = TransientDataAccessResourceException.class)
    public void readOnlyTransactionAttribute() {
        testUserService.getAll();
    }

    @Test
    public void advisorAutoProxyCreator() {
        assertThat(testUserService, is(instanceOf(Proxy.class)));
    }

    @Test(expected = TransientDataAccessResourceException.class)
    @Transactional(readOnly = true)
    public void transactionSync() {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    /**
     * 레벨을 업그레이드하는 도중 예외가 발생하는 경우를 테스트하기 위한 Mock 클래스
     */
    public static class TestUserServiceImpl extends UserServiceImpl {
        private String id = "ddd";

        @Override
        public List<User> getAll() {
            for (User user : super.getAll()) {
                super.update(user);
            }
            return null;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(id)) {
                throw new TestUserServiceException();
            }

            super.upgradeLevel(user);
        }
    }

    public static class TestUserServiceException extends RuntimeException {
        // empty
    }

    private static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        public MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<User> getAll() {
            return users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }
}
