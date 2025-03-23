package io.github.jeli01.kakao_bootcamp_community.user.exception;

public class NicknameDuplicatedException extends RuntimeException {
    public NicknameDuplicatedException(String message) {
        super(message);
    }
}
