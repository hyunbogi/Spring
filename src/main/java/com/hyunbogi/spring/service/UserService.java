package com.hyunbogi.spring.service;

import com.hyunbogi.spring.model.User;

import java.util.List;

public interface UserService {
    /**
     * User 정보를 추가한다.
     *
     * @param user 추가할 User 정보
     */
    void add(User user);

    /**
     * User 정보를 가져온다.
     *
     * @param id 가져올 User ID
     * @return User 정보
     */
    User get(String id);

    /**
     * 모든 User 정보를 가져온다.
     *
     * @return 가져온 User 정보 리스트
     */
    List<User> getAll();

    /**
     * 모든 User 정보를 삭제한다.
     */
    void deleteAll();

    /**
     * User 정보를 업데이트한다.
     *
     * @param user 업데이트할 User 정보
     */
    void update(User user);

    /**
     * 업그레이드 기준을 충족하는 User의 레벨을 업그레이드한다.
     */
    void upgradeLevels();
}
