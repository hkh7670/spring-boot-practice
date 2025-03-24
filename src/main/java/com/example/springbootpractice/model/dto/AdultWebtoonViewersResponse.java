package com.example.springbootpractice.model.dto;

import com.example.springbootpractice.model.entity.UserEntity;
import com.example.springbootpractice.model.enums.Gender;
import com.example.springbootpractice.model.enums.UserType;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AdultWebtoonViewersResponse(
    long userSeq,
    String userName,
    String userEmail,
    Gender gender,
    UserType userType,
    long viewCount
) {

    public static AdultWebtoonViewersResponse from(UserEntity entity) {
        return AdultWebtoonViewersResponse.builder()
            .userSeq(entity.getSeq())
            .userName(entity.getName())
            .userEmail(entity.getEmail())
            .gender(entity.getGender())
            .userType(entity.getType())
            .viewCount(entity.getAdultWebtoonViewCount())
            .build();
    }

}
