package io.github.jeli01.kakao_bootcamp_community.user.api;

import io.github.jeli01.kakao_bootcamp_community.common.dto.SuccessfulResponse;
import io.github.jeli01.kakao_bootcamp_community.common.exception.response.ErrorResponse;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PatchPasswordRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PatchUserBasicRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PostSignUpRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.response.GetUserResponse;
import io.github.jeli01.kakao_bootcamp_community.user.exception.NicknameDuplicatedException;
import io.github.jeli01.kakao_bootcamp_community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserApiController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ErrorResponse handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NicknameDuplicatedException.class)
    public ErrorResponse handleNicknameDuplicatedException(NicknameDuplicatedException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @GetMapping("/users/{userId}")
    public GetUserResponse getUser(@PathVariable("userId") Long userId) {
        User user = userService.getUser(userId);
        return new GetUserResponse(user);
    }

    @PostMapping("/users")
    public SuccessfulResponse signUp(@RequestParam("profile_image") MultipartFile profileImage,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam("nickname") String nickname) {
        PostSignUpRequest postSignUpRequest = new PostSignUpRequest(profileImage, email, password, nickname);
        userService.signUp(postSignUpRequest);
        return new SuccessfulResponse("user post success");
    }

    @PatchMapping("/users/{userId}")
    public SuccessfulResponse patchUserBasic(
            @RequestParam(value = "profile_image", required = false) MultipartFile profileImage,
            @RequestParam("nickname") String nickname,
            @PathVariable("userId") Long userId) {
        PatchUserBasicRequest patchUserBasicRequest = new PatchUserBasicRequest(profileImage, nickname);
        userService.patchUserBasic(patchUserBasicRequest, userId);
        return new SuccessfulResponse("update user-basic success");
    }

    @DeleteMapping("/users/{userId}")
    public SuccessfulResponse deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return new SuccessfulResponse("user delete success");
    }

    @PatchMapping("/users/{userId}/password")
    public SuccessfulResponse patchUserPassword(@RequestBody PatchPasswordRequest patchPasswordRequest,
                                                @PathVariable("userId") Long userId) {
        userService.patchUserPassword(patchPasswordRequest, userId);
        return new SuccessfulResponse("password update success");
    }
}
