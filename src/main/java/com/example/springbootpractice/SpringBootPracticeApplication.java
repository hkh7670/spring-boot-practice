package com.example.springbootpractice;

import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@Slf4j
public class SpringBootPracticeApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(
            SpringBootPracticeApplication.class,
            args
        );

        Environment environment = context.getEnvironment();
        Set<String> importList = getImportList(environment);
        log.info("===== spring.config.import 값 : {}", importList);
    }

    private static Set<String> getImportList(Environment environment) {
        Set<String> importList = new HashSet<>();
        int index = 0;
        String format = "spring.config.import[%d]";

        while (true) {
            String key = String.format(format, index++);
            String[] configFileNameArray = environment.getProperty(key, String[].class);

            // import한 config 파일이 더이상 없을 경우 종료
            if (configFileNameArray == null || configFileNameArray.length == 0) {
                break;
            }

            importList.add(configFileNameArray[0]);
        }
        return importList;
    }

}
