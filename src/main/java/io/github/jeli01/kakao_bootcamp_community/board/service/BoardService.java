package io.github.jeli01.kakao_bootcamp_community.board.service;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.board.dto.request.PostBoardRequest;
import io.github.jeli01.kakao_bootcamp_community.board.dto.request.PutBoardRequest;
import io.github.jeli01.kakao_bootcamp_community.board.repository.BoardRepository;
import io.github.jeli01.kakao_bootcamp_community.cloud.s3.FileUtils;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileUtils fileUtils;

    public List<Board> getBoards() {
        List<Board> all = boardRepository.findAllByDeleteDateIsNull();
        return all;
    }

    public void createBoard(PostBoardRequest postBoardRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User writer = userRepository.findByIdAndDeleteDateIsNull(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String fullPath = null;
        if (postBoardRequest.getImage() != null) {
            fullPath = fileUtils.storeFile(postBoardRequest.getImage());
        }

        String originName = null;
        if (postBoardRequest.getImage() != null) {
            originName = postBoardRequest.getImage().getOriginalFilename();
        }

        Board board = new Board(
                postBoardRequest.getTitle(),
                postBoardRequest.getContent(),
                fullPath,
                originName,
                writer,
                0L,
                0L,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );

        boardRepository.save(board);
    }

    public void updateBoard(Long id, PutBoardRequest putBoardRequest) {
        Board board = boardRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!board.getWriter().getId().equals(Long.parseLong(userId))) {
            throw new IllegalStateException("You are not the owner of this board");
        }

        String fullPath = board.getBoardImage();
        if (putBoardRequest.getImage() != null) {
            String boardImage = board.getBoardImage();
            fileUtils.deleteFile(boardImage);
            fullPath = fileUtils.storeFile(putBoardRequest.getImage());
        }

        String originName = null;
        if (putBoardRequest.getImage() != null) {
            originName = putBoardRequest.getImage().getOriginalFilename();
        }

        board.changeBoard(putBoardRequest.getTitle(), putBoardRequest.getContent(), fullPath,
                originName);
        boardRepository.save(board);
    }

    public void deleteBoard(Long id) {
        Board board = boardRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!board.getWriter().getId().equals(Long.parseLong(userId))) {
            throw new IllegalStateException("You are not the owner of this board");
        }

        board.softDelete();
        boardRepository.save(board);
    }

    public Board getBoard(Long id) {
        Board board = boardRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found or deleted"));
        board.plusVisitCount();
        return board;
    }
}
