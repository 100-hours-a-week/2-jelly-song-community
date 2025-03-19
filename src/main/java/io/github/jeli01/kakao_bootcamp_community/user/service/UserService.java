package io.github.jeli01.kakao_bootcamp_community.user.service;

import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PatchPasswordRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PatchUserBasicRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PostSignUpRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.response.PatchPasswordResponse;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import io.github.jeli01.kakao_bootcamp_community.util.file.FileStoreUtils;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileStoreUtils fileStoreUtils;

    public void signUp(PostSignUpRequest req) {
        Boolean existsNickname = userRepository.existsByNickname(req.getNickname());
        Boolean existsEmail = userRepository.existsByEmail(req.getEmail());

        if (existsNickname || existsEmail) {
            return;
        }

        MultipartFile profileImage = req.getProfileImage();
        String imagePath = fileStoreUtils.storeFile(profileImage);

        User user = new User(req.getEmail(), bCryptPasswordEncoder.encode(req.getPassword()),
                req.getNickname(), imagePath, "ROLE_USER", LocalDateTime.now(), LocalDateTime.now(), null);

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));
        user.delete();
    }

    public void patchUserBasic(PatchUserBasicRequest patchUserBasicRequest, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));

        String oldImagePath = user.getProfileImage();
        fileStoreUtils.deleteFile(oldImagePath);
        String imagePath = fileStoreUtils.storeFile(patchUserBasicRequest.getProfileImage());

        user.changeUserBasic(imagePath, patchUserBasicRequest.getNickname());
    }

    public PatchPasswordResponse patchUserPassword(PatchPasswordRequest patchPasswordRequest, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));
        user.changePassword(patchPasswordRequest.getPassword());
        return new PatchPasswordResponse();
    }
}
