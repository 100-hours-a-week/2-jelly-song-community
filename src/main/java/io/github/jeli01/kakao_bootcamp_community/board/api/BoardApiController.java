package io.github.jeli01.kakao_bootcamp_community.board.controller;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.board.dto.request.PostBoardRequest;
import io.github.jeli01.kakao_bootcamp_community.board.dto.request.PutBoardRequest;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.DeleteBoardResponse;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.GetBoardResponse;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.GetBoardsResponse;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.PostBoardResponse;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.PutBoardResponse;
import io.github.jeli01.kakao_bootcamp_community.board.repository.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;

    @GetMapping("/boards")
    public GetBoardsResponse getBoards() {
        List<Board> boards = boardRepository.findAll();
        GetBoardsResponse getBoardsResponse = new GetBoardsResponse();
        getBoardsResponse.setIsSuccess(true);
        getBoardsResponse.setMessage("boards get success");
        getBoardsResponse.setCount(boards.size());
    }

    @GetMapping("/boards/{id}")
    public GetBoardResponse getBoard(@PathVariable()) {
        return null;
    }

    @PostMapping("/boards")
    public PostBoardResponse postBoard(PostBoardRequest postBoardRequest) {
        return null;
    }

    @PutMapping("/boards/{id}")
    public PutBoardResponse putBoard(PutBoardRequest putBoardRequest) {
        return null;
    }

    @DeleteMapping("/boards/{id}")
    public DeleteBoardResponse deleteBoard(@PathVariable("id") Long id) {
        boardRepository.deleteById(id);
        return new DeleteBoardResponse();
    }
}
