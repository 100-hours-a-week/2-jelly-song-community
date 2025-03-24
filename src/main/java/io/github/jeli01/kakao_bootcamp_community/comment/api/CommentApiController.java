package io.github.jeli01.kakao_bootcamp_community.comment.api;

import io.github.jeli01.kakao_bootcamp_community.comment.dto.request.PostCommentRequest;
import io.github.jeli01.kakao_bootcamp_community.comment.dto.request.PutCommentRequest;
import io.github.jeli01.kakao_bootcamp_community.comment.service.CommentService;
import io.github.jeli01.kakao_bootcamp_community.common.dto.SuccessfulResponse;
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
    public SuccessfulResponse addComment(@PathVariable("boardId") Long boardId,
                                          @RequestBody PostCommentRequest request) {
        commentService.addComment(boardId, request.getContent());
        return new SuccessfulResponse("comment post success");
    }

    @PutMapping("/{commentId}")
    public SuccessfulResponse updateComment(@PathVariable("commentId") Long commentId,
                                            @RequestBody PutCommentRequest request) {
        commentService.updateComment(commentId, request.getContent());
        return new SuccessfulResponse("comment post success");
    }

    @DeleteMapping("/{commentId}")
    public SuccessfulResponse deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return new SuccessfulResponse("comment delete success");
    }
}
