package io.github.jeli01.kakao_bootcamp_community.cloud.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
