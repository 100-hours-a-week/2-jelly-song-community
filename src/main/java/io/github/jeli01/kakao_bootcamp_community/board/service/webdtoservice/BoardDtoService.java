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
import java.util.ArrayList;
import java.util.List;
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

    public GetBoardsResponse BoardsToDto(List<Board> boardList) {
        GetBoardsResponse getBoardsResponse = new GetBoardsResponse();
        getBoardsResponse.setIsSuccess(true);
        getBoardsResponse.setMessage("get boards success");
        getBoardsResponse.setCount(boardList.size());

        List<BoardsInnerData> list = new ArrayList<>();
        for (Board board : boardList) {
            BoardsInnerData boardsInnerData = new BoardsInnerData();
            boardsInnerData.setId(board.getId());
            boardsInnerData.setTitle(board.getTitle());
            boardsInnerData.setLike(board.getLikeCount());
            boardsInnerData.setCommentsCounts(board.getCommentCount());
            boardsInnerData.setVisitCounts(board.getVisitCount());
            boardsInnerData.setCreateDate(board.getCreateDate());
            boardsInnerData.setProfileImage(board.getWriter().getProfileImage());
            boardsInnerData.setWriter(board.getWriter().getNickname());
            list.add(boardsInnerData);
        }

        getBoardsResponse.setDatas(list);
        return getBoardsResponse;
    }

    public GetBoardResponse BoardToDto(Board board) {
        GetBoardResponse getBoardResponse = new GetBoardResponse();
        getBoardResponse.setIsSuccess(true);
        getBoardResponse.setMessage("get board success");

        DataInBoard data = new DataInBoard();
        data.setId(board.getId());
        data.setTitle(board.getTitle());

        data.setBoardImage(board.getBoardImage());
        data.setWriter(board.getWriter().getNickname());

        data.setCreateDate(board.getCreateDate());
        data.setContents(board.getContent());

        User writer = board.getWriter();
        String profileImage = writer.getProfileImage();
        data.setProfileImage(profileImage);

        Long likeCount = likeRepository.countByBoard(board);
        data.setLike(likeCount);
        data.setVisitCount(board.getVisitCount());

        Long commentCount = commentRepository.countByBoard(board);
        data.setCommentsCount(commentCount);

        List<Comment> commentList = commentRepository.findByBoardIdAndDeleteDateIsNull(board.getId());
        List<CommentInnerDataInBoard> comments = new ArrayList<>();

        for (Comment comment : commentList) {
            CommentInnerDataInBoard commentDto = new CommentInnerDataInBoard();
            commentDto.setId(comment.getId());
            commentDto.setProfileImage(comment.getWriter().getProfileImage());
            commentDto.setWriter(comment.getWriter().getNickname());
            commentDto.setCreateDate(comment.getCreateDate());
            commentDto.setContent(comment.getContent());
            comments.add(commentDto);
        }

        data.setComments(comments);

        getBoardResponse.setData(data);

        return getBoardResponse;
    }
}
