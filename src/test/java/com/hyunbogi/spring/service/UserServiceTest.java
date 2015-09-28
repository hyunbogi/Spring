package com.hyunbogi.spring.service;

import com.hyunbogi.spring.dao.UserDao;
import com.hyunbogi.spring.model.Level;
import com.hyunbogi.spring.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("ddd", "Jimin Han", "aaa12345", Level.BASIC, 40, 9),
                new User("eee", "Choa", "aaa12345", Level.BASIC, 50, 0),
                new User("fff", "Ailee", "aaa12345", Level.SILVER, 60, 29),
                new User("ggg", "Seulgi Kim", "aaa12345", Level.SILVER, 60, 30),
                new User("hhh", "Hyeri", "aaa12345", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        users.forEach(userDao::add);

        userService.upgradeLevels();
        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }
}
