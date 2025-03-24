package io.github.jeli01.kakao_bootcamp_community.like.api;

import io.github.jeli01.kakao_bootcamp_community.common.dto.SuccessfulResponse;
import io.github.jeli01.kakao_bootcamp_community.like.dto.response.GetLikeMeResponse;
import io.github.jeli01.kakao_bootcamp_community.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("likes/boards/{board_id}")
public class LikeApiController {
    private final LikeService likeService;

    @GetMapping("/users/{userId}")
    public GetLikeMeResponse getLikeMe(@PathVariable("board_id") Long board_id, @PathVariable("userId") Long userId) {
        boolean liked = likeService.isLiked(board_id, userId);
        return new GetLikeMeResponse(liked);
    }

    @PostMapping
    public SuccessfulResponse addLike(@PathVariable("board_id") Long board_id) {
        likeService.addLike(board_id);
        return new SuccessfulResponse("like post success");
    }

    @DeleteMapping
    public SuccessfulResponse removeLike(@PathVariable("board_id") Long board_id) {
        likeService.removeLike(board_id);
        return new SuccessfulResponse("like delete success");
    }
}
