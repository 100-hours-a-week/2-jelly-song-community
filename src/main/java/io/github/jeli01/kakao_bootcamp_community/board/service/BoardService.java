package io.github.jeli01.kakao_bootcamp_community.board.service;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.board.dto.request.PostBoardRequest;
import io.github.jeli01.kakao_bootcamp_community.board.dto.request.PutBoardRequest;
import io.github.jeli01.kakao_bootcamp_community.board.repository.BoardRepository;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import io.github.jeli01.kakao_bootcamp_community.util.file.FileStoreUtils;
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
    private final FileStoreUtils fileStoreUtils;

    public List<Board> getBoards() {
        List<Board> all = boardRepository.findAll();
        return all;
    }

    public void createBoard(PostBoardRequest postBoardRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User writer = userRepository.findByEmailAndDeleteDateIsNull(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String fullPath = fileStoreUtils.storeFile(postBoardRequest.getImage());

        Board board = new Board(
                postBoardRequest.getTitle(),
                postBoardRequest.getContent(),
                fullPath,
                writer,
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

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!board.getWriter().getEmail().equals(email)) {
            throw new IllegalStateException("You are not the owner of this board");
        }

        String fullPath = fileStoreUtils.storeFile(putBoardRequest.getImage());

        board.changeBoard(putBoardRequest.getTitle(), putBoardRequest.getContent(), fullPath);
        boardRepository.save(board);
    }

    public void deleteBoard(Long id) {
        Board board = boardRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!board.getWriter().getEmail().equals(email)) {
            throw new IllegalStateException("You are not the owner of this board");
        }

        board.softDelete();
        boardRepository.save(board);
    }

    public Board getBoard(Long id) {
        return boardRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found or deleted"));
    }
}
