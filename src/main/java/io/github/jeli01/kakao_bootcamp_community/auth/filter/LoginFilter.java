package io.github.jeli01.kakao_bootcamp_community.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jeli01.kakao_bootcamp_community.auth.domain.RefreshToken;
import io.github.jeli01.kakao_bootcamp_community.auth.dto.CustomUserDetails;
import io.github.jeli01.kakao_bootcamp_community.auth.jwt.JWTUtil;
import io.github.jeli01.kakao_bootcamp_community.auth.repository.RefreshTokenRepository;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PostLoginRequest;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                       RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        PostLoginRequest postLoginRequest = new PostLoginRequest();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            postLoginRequest = objectMapper.readValue(messageBody, PostLoginRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = postLoginRequest.getEmail();
        String password = postLoginRequest.getPassword();

        Long id = -1L;
        User user = userRepository.findByEmailAndDeleteDateIsNull(email).orElseThrow(() -> {
            throw new IllegalArgumentException("존재하지 않는 사용자 입니다.");
        });
        if (user != null) {
            id = user.getId();
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password,
                null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
        addRefreshEntity(username, refresh, 86400000L);

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
        refreshTokenRepository.save(refreshTokenEntity);
    }
}
