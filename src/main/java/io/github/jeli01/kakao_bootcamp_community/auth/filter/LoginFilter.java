package io.github.jeli01.kakao_bootcamp_community.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jeli01.kakao_bootcamp_community.auth.domain.RefreshToken;
import io.github.jeli01.kakao_bootcamp_community.auth.dto.CustomUserDetails;
import io.github.jeli01.kakao_bootcamp_community.auth.dto.request.PostLoginRequest;
import io.github.jeli01.kakao_bootcamp_community.auth.jwt.JWTUtil;
import io.github.jeli01.kakao_bootcamp_community.auth.service.RefreshTokenService;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    private static final long ACCESS_EXPIRATION_MS = 600_000L;
    private static final long REFRESH_EXPIRATION_MS = 86_400_000L;
    private static final Long unAuthorizedId = -1L;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                       RefreshTokenService refreshTokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        PostLoginRequest loginRequest = parseLoginRequest(request);

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Long id = unAuthorizedId;

        User user = userService.findByEmailAndDeleteDateIsNull(email);
        if (user != null) {
            id = user.getId();
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password, null);

        return authenticationManager.authenticate(authToken);
    }

    private PostLoginRequest parseLoginRequest(HttpServletRequest request) {
        try {
            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            return new ObjectMapper().readValue(messageBody, PostLoginRequest.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("로그인 요청 파싱 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        String role = obtainRole(authentication);

        String access = jwtUtil.createJwt("access", username, role, ACCESS_EXPIRATION_MS);
        String refresh = jwtUtil.createJwt("refresh", username, role, REFRESH_EXPIRATION_MS);
        addRefreshEntity(username, refresh, REFRESH_EXPIRATION_MS);

        writeJwtTokens(response, access, refresh);
    }

    private static String obtainRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        return role;
    }

    private void writeJwtTokens(HttpServletResponse response, String access, String refresh) throws IOException {
        response.setHeader("Authorization", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("isSuccess", "true");
        responseBody.put("message", "Login successful");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        writeUnsuccessfulAuthentication(response);
    }

    private static void writeUnsuccessfulAuthentication(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("isSuccess", "false");
        responseBody.put("message", "unsuccessfulAuthentication");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);

        response.getWriter().write(jsonResponse);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        LocalDateTime date = LocalDateTime.now().plus(Duration.ofMillis(expiredMs));
        RefreshToken refreshTokenEntity = new RefreshToken(username, refresh, date);
        refreshTokenService.save(refreshTokenEntity);
    }
}
