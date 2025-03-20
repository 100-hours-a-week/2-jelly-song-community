package io.github.jeli01.kakao_bootcamp_community.auth.filter;

import io.github.jeli01.kakao_bootcamp_community.auth.dto.CustomUserDetails;
import io.github.jeli01.kakao_bootcamp_community.auth.jwt.JWTUtil;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public JWTFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null || accessToken.length() <= 7 || !accessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring(7);
        System.out.println("accessToken : " + accessToken);
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String id = jwtUtil.getUsername(accessToken);
        Long parsedId = Long.parseLong(id);

        Optional<User> userOptional = userRepository.findByIdAndDeleteDateIsNull(parsedId);
        if (userOptional.isEmpty()) {
            PrintWriter writer = response.getWriter();
            writer.print("no user");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String role = jwtUtil.getRole(accessToken);
        User userEntity = new User(parsedId, role);
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
                customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
