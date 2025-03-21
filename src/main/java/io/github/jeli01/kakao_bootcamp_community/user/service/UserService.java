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

    public User getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        });
        return user;
    }


    public void signUp(PostSignUpRequest req) {
        Boolean existsNickname = userRepository.existsByNicknameAndDeleteDateIsNull(req.getNickname());
        Boolean existsEmail = userRepository.existsByEmailAndDeleteDateIsNull(req.getEmail());

        if (existsNickname) {
            throw new IllegalArgumentException("존재하는 닉네임 입니다.");
        }

        if (existsEmail) {
            throw new IllegalArgumentException("존재하는 이메일 입니다.");
        }

        MultipartFile profileImage = req.getProfileImage();
        String imagePath = fileStoreUtils.storeFile(profileImage);

        User user = new User(req.getEmail(), bCryptPasswordEncoder.encode(req.getPassword()),
                req.getNickname(), imagePath, "ROLE_USER", LocalDateTime.now(), LocalDateTime.now(), null);

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
        user.delete();
    }

    public void patchUserBasic(PatchUserBasicRequest patchUserBasicRequest, Long id) {
        User user = userRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));

        String oldImagePath = user.getProfileImage();
        fileStoreUtils.deleteFile(oldImagePath);
        String imagePath = fileStoreUtils.storeFile(patchUserBasicRequest.getProfileImage());

        user.changeUserBasic(imagePath, patchUserBasicRequest.getNickname());
    }

    public PatchPasswordResponse patchUserPassword(PatchPasswordRequest patchPasswordRequest, Long id) {
        User user = userRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
        user.changePassword(bCryptPasswordEncoder.encode(patchPasswordRequest.getPassword()));
        return new PatchPasswordResponse();
    }
}
