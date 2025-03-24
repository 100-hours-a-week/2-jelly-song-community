package io.github.jeli01.kakao_bootcamp_community.comment.repository;


import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.comment.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardIdAndDeleteDateIsNull(Long boardId);
    Optional<Comment> findByIdAndDeleteDateIsNull(Long id);
    Long countByBoardAndDeleteDateIsNull(Board board);

    List<Comment> findByWriterIdAndDeleteDateIsNull(Long id);
}
