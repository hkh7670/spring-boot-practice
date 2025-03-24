package com.example.springbootpractice.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.springbootpractice.exception.ApiErrorException;
import com.example.springbootpractice.exception.ErrorCode;
import com.example.springbootpractice.exception.ErrorResponse;
import com.example.springbootpractice.model.dto.LogInRequest;
import com.example.springbootpractice.model.dto.SignUpRequest;
import com.example.springbootpractice.model.enums.Gender;
import com.example.springbootpractice.model.enums.Role;
import com.example.springbootpractice.model.enums.UserType;
import com.example.springbootpractice.service.UserService;
import com.example.springbootpractice.service.WebtoonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private WebtoonService webtoonService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("회원 가입 실패 - 이메일 주소 패턴 매칭 실패 시 에러처리")
    void requestSignUpFailTest() throws Exception {
        // given
        SignUpRequest request = new SignUpRequest(
            "한규호",
            "ghhan@asdasd",
            "ghhan1234",
            Role.ROLE_USER,
            Gender.MALE,
            UserType.ADULT
        );
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/signup")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            // then
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                new ErrorResponse(ErrorCode.SCHEMA_VALIDATE_ERROR))));
    }

    @Test
    @DisplayName("회원 가입 성공")
    void requestSignUp() throws Exception {
        // given
        SignUpRequest request = new SignUpRequest(
            "한규호",
            "ghhan@abc.com",
            "ghhan12345",
            Role.ROLE_USER,
            Gender.MALE,
            UserType.ADULT
        );

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/signup")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            // then
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("JWT 토큰 발급 성공")
    void requestLogIn() throws Exception {
        // given
        LogInRequest request = new LogInRequest("ghhan@abc.com", "ghhan12345");
        userService.createUser(new SignUpRequest(
            "한규호",
            "ghhan@abc.com",
            "ghhan12345",
            Role.ROLE_USER,
            Gender.MALE,
            UserType.ADULT
        ));

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/login")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void requestDeleteMe() throws Exception {
        // given
        userService.createUser(new SignUpRequest(
            "한규호",
            "ghhan@abc.com",
            "ghhan12345",
            Role.ROLE_USER,
            Gender.MALE,
            UserType.ADULT
        ));
        LogInRequest logInRequest = new LogInRequest("ghhan@abc.com", "ghhan12345");
        String jwtToken = userService.getJwtToken(logInRequest);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/me")
            .header("Authorization", "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON));

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());

        // 탈퇴 회원 조회 시 NOT_FOUND_USER exception 발생
        assertThrows(ApiErrorException.class, () ->
            userService.getUserByEmail("ghhan@abc.com"));
    }

}