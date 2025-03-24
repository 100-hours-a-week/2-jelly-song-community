package io.github.jeli01.kakao_bootcamp_community.comment.service;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.board.repository.BoardRepository;
import io.github.jeli01.kakao_bootcamp_community.comment.domain.Comment;
import io.github.jeli01.kakao_bootcamp_community.comment.repository.CommentRepository;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void addComment(Long boardId, String content) {
        User user = getCurrentUser();
        Board board = getBoard(boardId);

        board.plusCommentCount();
        Comment comment = new Comment(
                content,
                board,
                user,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, String content) {
        User user = getCurrentUser();
        Comment comment = getComment(commentId);

        validateCommentOwner(comment, user);

        comment.changeContent(content);
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        User user = getCurrentUser();
        Comment comment = getComment(commentId);

        validateCommentOwner(comment, user);

        Board board = comment.getBoard();
        board.minusCommentCount();
        comment.softDelete();
    }

    private User getCurrentUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByIdAndDeleteDateIsNull(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findByIdAndDeleteDateIsNull(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findByIdAndDeleteDateIsNull(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }

    private void validateCommentOwner(Comment comment, User user) {
        if (!comment.getWriter().equals(user)) {
            throw new IllegalStateException("You are not the owner of this comment");
        }
    }
}
