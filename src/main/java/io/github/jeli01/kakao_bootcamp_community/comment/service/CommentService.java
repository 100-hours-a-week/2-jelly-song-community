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
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByIdAndDeleteDateIsNull(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Board board = boardRepository.findByIdAndDeleteDateIsNull(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

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
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByIdAndDeleteDateIsNull(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = commentRepository.findByIdAndDeleteDateIsNull(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getWriter().equals(user)) {
            throw new IllegalStateException("You are not the owner of this comment");
        }

        comment.changeContent(content);
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByIdAndDeleteDateIsNull(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = commentRepository.findByIdAndDeleteDateIsNull(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!comment.getWriter().equals(user)) {
            throw new IllegalStateException("You are not the owner of this comment");
        }

        comment.softDelete();
    }
}
