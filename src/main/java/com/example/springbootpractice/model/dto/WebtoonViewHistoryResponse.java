package com.example.springbootpractice.model.dto;

import com.example.springbootpractice.model.entity.UserEntity;
import com.example.springbootpractice.model.entity.WebtoonViewHistoryEntity;
import com.example.springbootpractice.model.enums.Gender;
import com.example.springbootpractice.model.enums.UserType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WebtoonViewHistoryResponse(
    long userSeq,
    String userName,
    String userEmail,
    Gender gender,
    UserType userType,
    LocalDateTime viewDateTime
) {

    public static WebtoonViewHistoryResponse from(WebtoonViewHistoryEntity entity) {
        UserEntity user = entity.getUser();
        return WebtoonViewHistoryResponse.builder()
            .userSeq(user.getSeq())
            .userName(user.getName())
            .userEmail(user.getEmail())
            .gender(user.getGender())
            .userType(user.getType())
            .viewDateTime(entity.getRegDate())
            .build();
    }

}
