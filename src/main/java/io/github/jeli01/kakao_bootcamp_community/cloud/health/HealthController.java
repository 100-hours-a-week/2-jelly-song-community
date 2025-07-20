package io.github.jeli01.kakao_bootcamp_community.cloud.health;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthController {
    @GetMapping
    public String health() {
        return "OK";
    }
}
