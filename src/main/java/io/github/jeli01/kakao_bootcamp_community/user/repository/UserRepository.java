package io.github.jeli01.kakao_bootcamp_community.user.repository;

import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByNicknameAndDeleteDateIsNull(String nickname);

    Optional<User> findByEmailAndDeleteDateIsNull(String email);

    boolean existsByEmailAndDeleteDateIsNull(String email);

    Optional<User> findByIdAndDeleteDateIsNull(Long id);
}
