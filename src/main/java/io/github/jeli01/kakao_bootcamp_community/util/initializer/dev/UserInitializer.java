package io.github.jeli01.kakao_bootcamp_community.util.initializer.dev;

import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import io.github.jeli01.kakao_bootcamp_community.util.initializer.exception.ExistsTestUserException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev-initialize")
public class UserInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${test.image.url}")
    private String testImageURL;

    @Value("${test.image.url2}")
    private String testImageURL2;

    @Override
    public void run(String... args) throws Exception {
        saveTestUser(createTestUser("test@naver.com", "Test1234!", "테스트닉네임", testImageURL));
        saveTestUser(createTestUser("test2@naver.com", "Test1234!", "테스트닉네임2", testImageURL2));
    }

    private User createTestUser(String email, String password, String nickname, String imageUrl) {
        return new User(
                email,
                passwordEncoder.encode(password),
                nickname,
                imageUrl,
                "ROLE_USER",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    private void saveTestUser(User user) {
        validateExistsEmailEqualsUser(user);
        validateExistsNicknameEqualsUser(user);
        userRepository.save(user);
        log.info("테스트 유저 자동 생성 완료: {}", user.getEmail());
    }

    private void validateExistsNicknameEqualsUser(User user) {
        Boolean exists = userRepository.existsByNicknameAndDeleteDateIsNull(user.getNickname());
        if (exists) {
            throw new ExistsTestUserException("이미 존재하는 닉네임입니다.");
        }
    }

    private void validateExistsEmailEqualsUser(User user) {
        boolean exists = userRepository.existsByEmailAndDeleteDateIsNull(user.getEmail());
        if (exists) {
            throw new ExistsTestUserException("이미 존재하는 이메일입니다.");
        }

    }

}
