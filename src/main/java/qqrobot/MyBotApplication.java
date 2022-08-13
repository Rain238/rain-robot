package qqrobot;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.spring.autoconfigure.EnableSimbot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import qqrobot.mark.LoadMicroMark;

@Slf4j
@EnableSimbot
@EnableScheduling
@SpringBootApplication
public class MyBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyBotApplication.class, args);
        log.info("机器人启动成功~~~~");
        new LoadMicroMark().logo();

    }
}
