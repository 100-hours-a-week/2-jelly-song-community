package io.github.jeli01.kakao_bootcamp_community.auth.config;

import io.github.jeli01.kakao_bootcamp_community.auth.filter.CustomLogoutFilter;
import io.github.jeli01.kakao_bootcamp_community.auth.filter.JWTFilter;
import io.github.jeli01.kakao_bootcamp_community.auth.filter.LoginFilter;
import io.github.jeli01.kakao_bootcamp_community.auth.handler.CustomAccessDeniedHandler;
import io.github.jeli01.kakao_bootcamp_community.auth.handler.CustomAuthenticationEntryPoint;
import io.github.jeli01.kakao_bootcamp_community.auth.jwt.JWTUtil;
import io.github.jeli01.kakao_bootcamp_community.auth.service.RefreshTokenService;
import io.github.jeli01.kakao_bootcamp_community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((auth) -> auth.disable());
        http.formLogin((auth) -> auth.disable());
        http.httpBasic((auth) -> auth.disable());

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(HttpMethod.GET,
                        "/", "/favicon.ico", "/css/**", "/js/**", "/images/**", "/layout/**", "/**.html", "/boards/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/login", "/logout", "/users", "/reissue").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated());

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JWTFilter(jwtUtil, userService), LoginFilter.class);
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenService), LogoutFilter.class);
        http.addFilterAt(
                new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenService,
                        userService), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
        );

        return http.build();
    }

}
