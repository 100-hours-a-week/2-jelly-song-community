package io.github.jeli01.kakao_bootcamp_community.like.service;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.board.repository.BoardRepository;
import io.github.jeli01.kakao_bootcamp_community.like.domain.Like;
import io.github.jeli01.kakao_bootcamp_community.like.repository.LikeRepository;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void addLike(Long boardId) {
        User user = getCurrentUser();
        Board board = getBoard(boardId);
        validateUserDidlike(user, board);

        board.plusLikeCount();

        Like like = new Like(user, board);
        likeRepository.save(like);
    }

    private void validateUserDidlike(User user, Board board) {
        Optional<Like> existingLike = likeRepository.findByUserAndBoard(user, board);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("You already liked this post");
        }
    }

    public void removeLike(Long boardId) {
        User user = getCurrentUser();
        Board board = getBoard(boardId);

        validateDidNotLike(user, board);

        board.minusLikeCount();
        likeRepository.deleteByUserAndBoard(user, board);
    }

    private void validateDidNotLike(User user, Board board) {
        Optional<Like> existingLike = likeRepository.findByUserAndBoard(user, board);
        if (existingLike.isEmpty()) {
            throw new IllegalStateException("You have not liked this post");
        }
    }

    public boolean isLiked(Long boardId, Long requestUserId) {
        User user = getCurrentUser();
        Board board = getBoard(boardId);

        return likeRepository.findByUserAndBoard(user, board).isPresent();
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
}
