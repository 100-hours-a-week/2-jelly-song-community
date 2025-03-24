package io.github.jeli01.kakao_bootcamp_community.like.repository;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.like.domain.Like;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByBoard(Board board);
    Optional<Like> findByUserAndBoard(User user, Board board);
    void deleteByUserAndBoard(User user, Board board);

    List<Like> findByUserAndDeleteDateIsNull(User user);

    List<Like> findByBoardAndDeleteDateIsNull(Board board);
}
