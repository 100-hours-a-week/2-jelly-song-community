package io.github.jeli01.kakao_bootcamp_community.user.repository;

import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
