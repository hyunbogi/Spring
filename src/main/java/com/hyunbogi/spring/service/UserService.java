package com.hyunbogi.spring.service;

import com.hyunbogi.spring.model.User;

public interface UserService {
    /**
     * User 정보를 추가한다.
     *
     * @param user 추가할 User 정보
     */
    void add(User user);

    /**
     * 업그레이드 기준을 충족하는 User의 레벨을 업그레이드한다.
     */
    void upgradeLevels();
}
