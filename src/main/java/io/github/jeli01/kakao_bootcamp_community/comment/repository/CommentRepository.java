package io.github.jeli01.kakao_bootcamp_community.comment.repository;


import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countByBoard(Board board);

    List<Comment> findByBoardIdAndDeleteDateIsNull(Long boardId);
}
