package io.github.jeli01.kakao_bootcamp_community.board.service.webdtoservice;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.board.dto.BoardsInnerData;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.GetBoardResponse;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.GetBoardResponse.DataInBoard;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.GetBoardResponse.DataInBoard.CommentInnerDataInBoard;
import io.github.jeli01.kakao_bootcamp_community.board.dto.response.GetBoardsResponse;
import io.github.jeli01.kakao_bootcamp_community.board.repository.BoardRepository;
import io.github.jeli01.kakao_bootcamp_community.comment.domain.Comment;
import io.github.jeli01.kakao_bootcamp_community.comment.repository.CommentRepository;
import io.github.jeli01.kakao_bootcamp_community.like.repository.LikeRepository;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardDtoService {
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public GetBoardsResponse mapBoardsToDto(List<Board> boardList) {
        List<BoardsInnerData> datas = boardList.stream()
                .map(this::mapToBoardsInnerData)
                .collect(Collectors.toList());

        GetBoardsResponse response = new GetBoardsResponse("get boards success", datas.size(), datas);
        return response;
    }

    private BoardsInnerData mapToBoardsInnerData(Board board) {
        User writer = board.getWriter();

        BoardsInnerData dto = new BoardsInnerData();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setLike(board.getLikeCount());
        dto.setCommentsCounts(board.getCommentCount());
        dto.setVisitCounts(board.getVisitCount());
        dto.setCreateDate(board.getCreateDate());
        dto.setProfileImage(writer.getProfileImage());
        dto.setWriter(writer.getNickname());

        return dto;
    }

    public GetBoardResponse mapBoardToDto(Board board) {
        DataInBoard data = mapToDataInBoard(board);

        GetBoardResponse response = new GetBoardResponse();
        response.setIsSuccess(true);
        response.setMessage("get board success");
        response.setData(data);

        return response;
    }

    private DataInBoard mapToDataInBoard(Board board) {
        User writer = board.getWriter();

        DataInBoard dto = new DataInBoard();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setBoardImage(board.getBoardImage());
        dto.setOriginImageName(board.getBoardImageOriginName());
        dto.setWriter(writer.getNickname());
        dto.setWriterId(writer.getId());
        dto.setProfileImage(writer.getProfileImage());
        dto.setCreateDate(board.getCreateDate());
        dto.setContents(board.getContent());

        dto.setLike(likeRepository.countByBoard(board));
        dto.setVisitCount(board.getVisitCount());
        dto.setCommentsCount(commentRepository.countByBoardAndDeleteDateIsNull(board));

        List<Comment> comments = commentRepository.findByBoardIdAndDeleteDateIsNull(board.getId());
        dto.setComments(comments.stream()
                .map(this::mapToCommentDto)
                .collect(Collectors.toList())
        );

        return dto;
    }

    private CommentInnerDataInBoard mapToCommentDto(Comment comment) {
        User writer = comment.getWriter();

        CommentInnerDataInBoard dto = new CommentInnerDataInBoard();
        dto.setId(comment.getId());
        dto.setProfileImage(writer.getProfileImage());
        dto.setWriter(writer.getNickname());
        dto.setWriterId(writer.getId());
        dto.setCreateDate(comment.getCreateDate());
        dto.setContent(comment.getContent());

        return dto;
    }
}
