package io.github.jeli01.kakao_bootcamp_community.auth.service;

import io.github.jeli01.kakao_bootcamp_community.auth.dto.CustomUserDetails;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository.findByEmail(username);
        if (userData == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        if (userData.getDeleteDate() != null) {
            throw new UsernameNotFoundException("Deleted User : " + username);
        }
        return new CustomUserDetails(userData);
    }
}
