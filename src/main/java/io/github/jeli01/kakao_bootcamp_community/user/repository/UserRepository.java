package io.github.jeli01.kakao_bootcamp_community.user.repository;

import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByNickname(String nickname);

    Boolean existsByEmail(String email);

    User findByEmail(String email);

    @Transactional
    void deleteByProfileImage(String profileImage);
}
