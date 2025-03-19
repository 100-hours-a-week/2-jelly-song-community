package io.github.jeli01.kakao_bootcamp_community.comment.api;

import io.github.jeli01.kakao_bootcamp_community.comment.dto.request.PostCommentRequest;
import io.github.jeli01.kakao_bootcamp_community.comment.dto.request.PutCommentRequest;
import io.github.jeli01.kakao_bootcamp_community.comment.dto.response.DeleteCommentResponse;
import io.github.jeli01.kakao_bootcamp_community.comment.dto.response.PostCommentResponse;
import io.github.jeli01.kakao_bootcamp_community.comment.dto.response.PutCommentResponse;
import io.github.jeli01.kakao_bootcamp_community.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/comments")
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping
    public PostCommentResponse addComment(@PathVariable("boardId") Long boardId,
                                          @RequestBody PostCommentRequest request) {
        commentService.addComment(boardId, request.getContent());
        return new PostCommentResponse();
    }

    @PutMapping("/{commentId}")
    public PutCommentResponse updateComment(@PathVariable("commentId") Long commentId,
                                            @RequestBody PutCommentRequest request) {
        commentService.updateComment(commentId, request.getContent());
        return new PutCommentResponse();
    }

    @DeleteMapping("/{commentId}")
    public DeleteCommentResponse deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return new DeleteCommentResponse();
    }
}
