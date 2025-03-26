package io.github.jeli01.kakao_bootcamp_community.board.api;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.board.dto.request.PostBoardRequest;
import io.github.jeli01.kakao_bootcamp_community.board.dto.request.PutBoardRequest;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.GetBoardResponse;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.GetBoardsResponse;
import io.github.jeli01.kakao_bootcamp_community.board.service.BoardService;
import io.github.jeli01.kakao_bootcamp_community.board.service.webdtoservice.BoardDtoService;
import io.github.jeli01.kakao_bootcamp_community.common.dto.SuccessfulResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardApiController {
    private final BoardService boardService;
    private final BoardDtoService boardDtoService;

    @GetMapping
    public GetBoardsResponse getBoards() {
        List<Board> boards = boardService.getBoards();
        return boardDtoService.mapBoardsToDto(boards);
    }

    @GetMapping("/{boardId}")
    public GetBoardResponse getBoard(@PathVariable("boardId") Long boardId) {
        Board board = boardService.getBoard(boardId);
        return boardDtoService.mapBoardToDto(board);
    }

    @PostMapping
    public SuccessfulResponse postBoard(@RequestParam(required = false) MultipartFile image,
                                        @RequestParam("title") String title,
                                        @RequestParam("content") String content) {
        PostBoardRequest postBoardRequest = new PostBoardRequest(title, content, image);
        boardService.createBoard(postBoardRequest);
        return new SuccessfulResponse("board create success");
    }

    @PutMapping("/{boardId}")
    public SuccessfulResponse putBoard(@PathVariable("boardId") Long boardId,
                                       @RequestParam(required = false) MultipartFile image,
                                       @RequestParam("title") String title,
                                       @RequestParam("content") String content) {
        PutBoardRequest putBoardRequest = new PutBoardRequest(title, content, image);
        boardService.updateBoard(boardId, putBoardRequest);
        return new SuccessfulResponse("board update success");
    }

    @DeleteMapping("{boardId}")
    public SuccessfulResponse deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return new SuccessfulResponse("board delete success");
    }
}
