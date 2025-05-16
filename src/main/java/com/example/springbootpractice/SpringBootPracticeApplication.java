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
        Set<String> configImportSet = getConfigImportSet(environment);
        log.info("===== spring.config.import 값 : {}", configImportSet);
    }

    private static Set<String> getConfigImportSet(Environment environment) {
        Set<String> configImportSet = new HashSet<>();
        String format = "spring.config.import[%d]";
        int index = 0;

        while (true) {
            String key = format.formatted(index++);
            String[] configFileNameArray = environment.getProperty(key, String[].class);

            // import한 config 파일이 더이상 없을 경우 종료
            if (configFileNameArray == null || configFileNameArray.length == 0) {
                break;
            }

            configImportSet.add(configFileNameArray[0]);
        }
        return configImportSet;
    }

}
