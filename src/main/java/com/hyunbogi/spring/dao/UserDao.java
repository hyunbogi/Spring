package com.hyunbogi.spring.dao;

import com.hyunbogi.spring.model.User;

import java.util.List;

public interface UserDao {
    /**
     * 새로운 User를 DB에 추가한다.
     *
     * @param user
     */
    void add(User user);

    /**
     * 해당 id를 가진 User 정보를 DB에서 가져온다.
     *
     * @param id
     * @return User 정보
     */
    User get(String id);

    /**
     * DB에 저장된 모든 User 정보를 가져온다.
     *
     * @return User 리스트
     */
    List<User> getAll();

    /**
     * User 정보를 DB에 수정하여 업데이트한다.
     */
    void update(User user);

    /**
     * DB의 모든 User 정보를 삭제한다.
     */
    void deleteAll();

    /**
     * DB에 저장된 모든 User의 수를 가져온다.
     *
     * @return DB에 저장된 모든 User의 수
     */
    int getCount();
}
