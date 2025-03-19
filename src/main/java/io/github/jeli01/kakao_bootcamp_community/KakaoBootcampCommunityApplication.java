package io.github.jeli01.kakao_bootcamp_community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KakaoBootcampCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakaoBootcampCommunityApplication.class, args);
    }

}
