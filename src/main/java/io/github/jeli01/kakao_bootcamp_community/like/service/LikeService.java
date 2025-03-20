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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailAndDeleteDateIsNull(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Board board = boardRepository.findByIdAndDeleteDateIsNull(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndBoard(user, board);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("You already liked this post");
        }

        Like like = new Like(user, board);
        likeRepository.save(like);
    }

    public void removeLike(Long boardId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailAndDeleteDateIsNull(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Board board = boardRepository.findByIdAndDeleteDateIsNull(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndBoard(user, board);
        if (existingLike.isEmpty()) {
            throw new IllegalStateException("You have not liked this post");
        }

        likeRepository.deleteByUserAndBoard(user, board);
    }
}
