package io.github.jeli01.kakao_bootcamp_community.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jeli01.kakao_bootcamp_community.auth.dto.CustomUserDetails;
import io.github.jeli01.kakao_bootcamp_community.auth.jwt.JWTUtil;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserService userService;

    public JWTFilter(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationValue = request.getHeader("Authorization");
        if (authorizationValue == null || authorizationValue.length() <= 7 || !authorizationValue.startsWith(
                "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = authorizationValue.substring(7);

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Access token expired");
            return;
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token");
            return;
        }

        Long userId = Long.parseLong(jwtUtil.getUsername(accessToken));
        Boolean existsUser = userService.existsByIdAndDeleteDateIsNull(userId);
        if (!existsUser) {
            sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found for access token");
            return;
        }

        setAuthentication(userId, jwtUtil.getRole(accessToken));

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(Long userId, String role) {
        User user = new User(userId, role);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void sendJsonError(HttpServletResponse response, int status, String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("isSuccess", false);
        errorResponse.put("stateCode", status);
        errorResponse.put("message", message);

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}

