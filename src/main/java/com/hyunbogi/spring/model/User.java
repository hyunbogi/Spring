package com.hyunbogi.spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private Level level;

    @Getter
    @Setter
    private int login;

    @Getter
    @Setter
    private int recommend;

    @Getter
    @Setter
    private String email;

    public void upgradeLevel() {
        Level nextLevel = level.nextLevel();

        if (nextLevel == null) {
            throw new IllegalStateException("Cannot upgrade " + level);
        }

        level = nextLevel;
    }
}
