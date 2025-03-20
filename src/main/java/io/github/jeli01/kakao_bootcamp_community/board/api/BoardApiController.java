package io.github.jeli01.kakao_bootcamp_community.board.api;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.board.dto.request.PostBoardRequest;
import io.github.jeli01.kakao_bootcamp_community.board.dto.request.PutBoardRequest;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.DeleteBoardResponse;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.GetBoardResponse;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.GetBoardsResponse;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.PostBoardResponse;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.PutBoardResponse;
import io.github.jeli01.kakao_bootcamp_community.board.service.BoardService;
import io.github.jeli01.kakao_bootcamp_community.board.service.webdtoservice.BoardDtoService;
import io.github.jeli01.kakao_bootcamp_community.exception.response.ErrorResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardApiController {
    private final BoardService boardService;
    private final BoardDtoService boardDtoService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse illegalExHandle(IllegalArgumentException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ErrorResponse handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @GetMapping
    public GetBoardsResponse getBoards() {
        List<Board> boards = boardService.getBoards();
        return boardDtoService.BoardsToDto(boards);
    }

    @GetMapping("/{id}")
    public GetBoardResponse getBoard(@PathVariable("id") Long id) {
        Board board = boardService.getBoard(id);
        return boardDtoService.BoardToDto(board);
    }

    @PostMapping
    public PostBoardResponse postBoard(@RequestParam MultipartFile image, @RequestParam("title") String title,
                                       @RequestParam("title") String content) {
        PostBoardRequest postBoardRequest = new PostBoardRequest();
        postBoardRequest.setImage(image);
        postBoardRequest.setTitle(title);
        postBoardRequest.setContent(content);
        boardService.createBoard(postBoardRequest);
        return new PostBoardResponse();
    }

    @PutMapping("/{id}")
    public PutBoardResponse putBoard(@PathVariable("id") Long id, @RequestParam MultipartFile image,
                                     @RequestParam("title") String title,
                                     @RequestParam("content") String content) {
        PutBoardRequest putBoardRequest = new PutBoardRequest();
        putBoardRequest.setImage(image);
        putBoardRequest.setTitle(title);
        putBoardRequest.setContent(content);

        boardService.updateBoard(id, putBoardRequest);
        return new PutBoardResponse();
    }

    @DeleteMapping("{id}")
    public DeleteBoardResponse deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return new DeleteBoardResponse();
    }
}
