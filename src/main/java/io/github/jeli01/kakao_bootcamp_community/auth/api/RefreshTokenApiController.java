package io.github.jeli01.kakao_bootcamp_community.auth.api;

import io.github.jeli01.kakao_bootcamp_community.auth.domain.RefreshToken;
import io.github.jeli01.kakao_bootcamp_community.auth.jwt.JWTUtil;
import io.github.jeli01.kakao_bootcamp_community.auth.service.RefreshTokenService;
import io.github.jeli01.kakao_bootcamp_community.exception.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshTokenApiController {
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse illegalExHandle(IllegalArgumentException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = findRefresh(request);

        validateRefreshExists(refresh);
        validateExpired(refresh);
        validateCategory(refresh);
        validateIsInServerStore(refresh);

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 8640000L);

        refreshTokenService.deleteByRefresh(refresh);
        saveRefreshToken(username, newRefresh, 8640000L);

        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateIsInServerStore(String refresh) {
        Boolean isExist = refreshTokenService.existsByRefresh(refresh);
        if (!isExist) {
            throw new IllegalArgumentException("invalid refresh token");
        }
    }

    private void validateCategory(String refresh) {
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new IllegalArgumentException("it is not refresh token category");
        }
    }

    private void validateExpired(String refresh) {
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("refresh token expired");
        }
    }

    private static void validateRefreshExists(String refresh) {
        if (Objects.isNull(refresh)) {
            throw new IllegalArgumentException("no exists refresh token");
        }
    }

    private String findRefresh(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }
        return refresh;
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void saveRefreshToken(String email, String refresh, Long expiredMs) {
        LocalDateTime date = LocalDateTime.now().plus(Duration.ofMillis(expiredMs));
        RefreshToken refreshTokenEntity = new RefreshToken(email, refresh, date);
        refreshTokenService.save(refreshTokenEntity);
    }
}
