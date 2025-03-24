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
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileUtils fileUtils;

    public List<Board> getBoards() {
        return boardRepository.findAllByDeleteDateIsNull();
    }

    public Board getBoard(Long id) {
        Board board = boardRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found or deleted"));
        board.plusVisitCount();
        return board;
    }

    public void createBoard(PostBoardRequest dto) {
        User writer = getCurrentUser();

        String imageUrl = null;
        String originName = null;

        MultipartFile image = dto.getImage();
        if (image != null) {
            imageUrl = fileUtils.storeFile(image);
            originName = image.getOriginalFilename();
        }

        Board board = new Board(
                dto.getTitle(),
                dto.getContent(),
                imageUrl,
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

    public void updateBoard(Long id, PutBoardRequest dto) {
        Board board = getBoardById(id);
        validateBoardOwner(board);

        String imageUrl = board.getBoardImage();
        String originName = board.getBoardImageOriginName();

        MultipartFile image = dto.getImage();

        if (image != null) {
            fileUtils.deleteFile(board.getBoardImage());
            imageUrl = fileUtils.storeFile(dto.getImage());
            originName = dto.getImage().getOriginalFilename();
        }

        board.changeBoard(dto.getTitle(), dto.getContent(), imageUrl, originName);
        boardRepository.save(board);
    }

    public void deleteBoard(Long id) {
        Board board = getBoardById(id);
        validateBoardOwner(board);

        board.softDelete();
        boardRepository.save(board);
    }

    private User getCurrentUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByIdAndDeleteDateIsNull(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Board getBoardById(Long id) {
        return boardRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
    }

    private void validateBoardOwner(Board board) {
        Long currentUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!board.getWriter().getId().equals(currentUserId)) {
            throw new IllegalStateException("You are not the owner of this board");
        }
    }
}
