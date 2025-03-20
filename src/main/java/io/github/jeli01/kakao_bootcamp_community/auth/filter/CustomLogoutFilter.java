package io.github.jeli01.kakao_bootcamp_community.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jeli01.kakao_bootcamp_community.auth.jwt.JWTUtil;
import io.github.jeli01.kakao_bootcamp_community.auth.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.filter.GenericFilterBean;

public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        if (!requestUri.matches("^\\/logout$") || !requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = getRefresh(request);
        if (refresh == null) {
            makeLogoutFailResponse(response);
            return;
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            makeLogoutFailResponse(response);
            return;
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            makeLogoutFailResponse(response);
            return;
        }

        Boolean isExist = refreshTokenRepository.existsByRefresh(refresh);
        if (!isExist) {
            makeLogoutFailResponse(response);
            return;
        }

        refreshTokenRepository.deleteByRefresh(refresh);
        removeCookie(response);
        makeSuccessJsonResponse(response);
    }

    private static void makeLogoutFailResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("isSuccess", "false");
        responseBody.put("message", "logout fail");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(jsonResponse);
    }

    private static void makeSuccessJsonResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("isSuccess", "true");
        responseBody.put("message", "logout success");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(jsonResponse);
    }

    private static void removeCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static String getRefresh(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }
        return refresh;
    }
}
