package io.github.jeli01.kakao_bootcamp_community.board.repository;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdAndDeleteDateIsNull(Long id);

    List<Board> findAllByDeleteDateIsNull();

    List<Board> findByWriterIdAndDeleteDateIsNull(Long id);

}
