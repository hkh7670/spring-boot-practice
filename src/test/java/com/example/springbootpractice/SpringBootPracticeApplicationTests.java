package com.example.springbootpractice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // application-test.yml 사용
class SpringBootPracticeApplicationTests {

    @Test
    void contextLoads() {
    }

}
