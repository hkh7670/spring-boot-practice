package com.example.springbootpractice.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.springbootpractice.exception.ApiErrorException;
import com.example.springbootpractice.exception.ErrorCode;
import com.example.springbootpractice.model.dto.LogInRequest;
import com.example.springbootpractice.model.dto.SignUpRequest;
import com.example.springbootpractice.model.dto.UpdateWebtoonCoinRequest;
import com.example.springbootpractice.model.dto.WebtoonEvaluationRequest;
import com.example.springbootpractice.model.enums.Gender;
import com.example.springbootpractice.model.enums.Role;
import com.example.springbootpractice.model.enums.UserType;
import com.example.springbootpractice.model.enums.WebtoonEvaluationType;
import com.example.springbootpractice.repository.WebtoonRepository;
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
class WebtoonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private WebtoonService webtoonService;

    @Autowired
    private WebtoonRepository webtoonRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("웹툰 평가 API")
    void requestEvaluationTest() throws Exception {
        // given
        String email = "ghhan@abc.com";
        userService.createUser(new SignUpRequest(
            "한규호",
            email,
            "ghhan12345",
            Role.ROLE_USER,
            Gender.MALE,
            UserType.ADULT
        ));
        LogInRequest logInRequest = new LogInRequest("ghhan@abc.com", "ghhan12345");
        String jwtToken = userService.getJwtToken(logInRequest);

        // when
        ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/webtoon/1/evaluation")
                .header("Authorization", "Bearer " + jwtToken)
                .content(objectMapper.writeValueAsString(
                    new WebtoonEvaluationRequest(WebtoonEvaluationType.LIKE, "좋아요!!")))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("좋아요/싫어요 Top3 웹툰 조회")
    void requestTop3WebtoonsTest() throws Exception {
        // given
        String email = "ghhan@abc.com";
        userService.createUser(new SignUpRequest(
            "한규호",
            email,
            "ghhan12345",
            Role.ROLE_USER,
            Gender.MALE,
            UserType.ADULT
        ));
        LogInRequest logInRequest = new LogInRequest("ghhan@abc.com", "ghhan12345");
        String jwtToken = userService.getJwtToken(logInRequest);

        // when
        ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/webtoon/top3")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("웹툰 금액 변경 성공")
    void requestUpdateWebtoonCoin() throws Exception {
        // given
        String email = "ghhan@abc.com";
        userService.createUser(new SignUpRequest(
            "한규호",
            email,
            "ghhan12345",
            Role.ROLE_ADMIN,
            Gender.MALE,
            UserType.ADULT
        ));
        LogInRequest logInRequest = new LogInRequest(email, "ghhan12345");
        String jwtToken = userService.getJwtToken(logInRequest);
        long webtoonSeq = 1L;
        long coin = 300L;

        // when
        ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/v1/webtoon/{webtoonSeq}/coin", webtoonSeq)
                .header("Authorization", "Bearer " + jwtToken)
                .content(objectMapper.writeValueAsString(new UpdateWebtoonCoinRequest(coin)))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(webtoonRepository.findById(webtoonSeq)
            .orElseThrow(() -> new ApiErrorException(ErrorCode.NOT_FOUND_WEBTOON_INFO))
            .getCoin(), coin);
    }

}