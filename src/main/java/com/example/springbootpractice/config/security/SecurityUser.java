package com.example.springbootpractice.config.security;

import com.example.springbootpractice.model.entity.UserEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SecurityUser extends User {

    private final UserEntity userEntity;

    public SecurityUser(UserEntity userEntity) {
        super(userEntity.getEmail(), userEntity.getPassword(),
            AuthorityUtils.createAuthorityList(userEntity.getRole().toString()));
        this.userEntity = userEntity;
    }

    public UserEntity getUser() {
        return this.userEntity;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}