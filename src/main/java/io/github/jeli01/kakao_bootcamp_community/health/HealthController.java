package io.github.jeli01.kakao_bootcamp_community.health;

import org.springframework.web.bind.annotation.RestController;

@RestController("/health")
public class HealthController {

    public String health() {
        return "ok";
    }
}
