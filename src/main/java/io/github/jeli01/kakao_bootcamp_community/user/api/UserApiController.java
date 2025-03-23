package io.github.jeli01.kakao_bootcamp_community.user.api;

import io.github.jeli01.kakao_bootcamp_community.common.exception.response.ErrorResponse;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PatchPasswordRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PatchUserBasicRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PostSignUpRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.response.DeleteUserResponse;
import io.github.jeli01.kakao_bootcamp_community.user.dto.response.GetUserResponse;
import io.github.jeli01.kakao_bootcamp_community.user.dto.response.PatchPasswordResponse;
import io.github.jeli01.kakao_bootcamp_community.user.dto.response.PatchUserBasicResponse;
import io.github.jeli01.kakao_bootcamp_community.user.dto.response.PostSignUpResponse;
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
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse illegalExHandle(IllegalArgumentException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

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

    @GetMapping("/users/{id}")
    public GetUserResponse getUser(@PathVariable("id") Long id) {
        User user = userService.getUser(id);
        return new GetUserResponse(user);
    }

    @PostMapping("/users")
    public PostSignUpResponse signUp(@RequestParam("profile_image") MultipartFile profileImage,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam("nickname") String nickname) {
        PostSignUpRequest postSignUpRequest = new PostSignUpRequest();
        postSignUpRequest.setProfileImage(profileImage);
        postSignUpRequest.setEmail(email);
        postSignUpRequest.setPassword(password);
        postSignUpRequest.setNickname(nickname);
        userService.signUp(postSignUpRequest);
        return new PostSignUpResponse();
    }

    @PatchMapping("/users/{id}")
    public PatchUserBasicResponse patchUserBasic(
            @RequestParam(value = "profile_image", required = false) MultipartFile profileImage,
            @RequestParam("nickname") String nickname,
            @PathVariable("id") Long id) {
        PatchUserBasicRequest patchUserBasicRequest = new PatchUserBasicRequest();
        patchUserBasicRequest.setProfileImage(profileImage);
        patchUserBasicRequest.setNickname(nickname);
        userService.patchUserBasic(patchUserBasicRequest, id);
        return new PatchUserBasicResponse();
    }

    @DeleteMapping("/users/{id}")
    public DeleteUserResponse deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new DeleteUserResponse();
    }

    @PatchMapping("/users/{id}/password")
    public PatchPasswordResponse patchUserPassword(@RequestBody PatchPasswordRequest patchPasswordRequest,
                                                   @PathVariable("id") Long id) {
        userService.patchUserPassword(patchPasswordRequest, id);
        return new PatchPasswordResponse();
    }
}
