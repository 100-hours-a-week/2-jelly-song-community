package io.github.jeli01.kakao_bootcamp_community.like.api;

import io.github.jeli01.kakao_bootcamp_community.common.exception.response.ErrorResponse;
import io.github.jeli01.kakao_bootcamp_community.like.dto.response.DeleteLikeResponse;
import io.github.jeli01.kakao_bootcamp_community.like.dto.response.GetLikeMeResponse;
import io.github.jeli01.kakao_bootcamp_community.like.dto.response.PostLikeResponse;
import io.github.jeli01.kakao_bootcamp_community.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("likes/boards/{id}")
public class LikeApiController {
    private final LikeService likeService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse illegalExHandle(IllegalArgumentException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @GetMapping("/users/{userId}")
    public GetLikeMeResponse getLikeMe(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        boolean liked = likeService.isLiked(id, userId);
        return new GetLikeMeResponse(liked);
    }

    @PostMapping
    public PostLikeResponse addLike(@PathVariable("id") Long id) {
        likeService.addLike(id);
        return new PostLikeResponse();
    }

    @DeleteMapping
    public DeleteLikeResponse removeLike(@PathVariable("id") Long id) {
        likeService.removeLike(id);
        return new DeleteLikeResponse();
    }
}
