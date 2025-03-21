package io.github.jeli01.kakao_bootcamp_community.util.initializer.dev;

import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String testEmail = "test@naver.com";
        User testUser = new User(
                testEmail,
                passwordEncoder.encode("Test1234!"),
                "테스트닉네임",
                "/Users/user/Desktop/image/63b651e7-284e-414b-bb76-8dc6390d7772.png",
                "ROLE_USER",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
        userRepository.save(testUser);
        log.info("테스트 유저 자동 생성 완료: {}", testUser.getEmail());
    }
}
