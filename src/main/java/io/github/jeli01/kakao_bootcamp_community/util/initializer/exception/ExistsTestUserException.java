package io.github.jeli01.kakao_bootcamp_community.util.initializer.exception;

public class ExistsTestUserException extends RuntimeException{
    public ExistsTestUserException(String email) {
        super("이미 존재하는 테스트 유저입니다: " + email);
    }
}
