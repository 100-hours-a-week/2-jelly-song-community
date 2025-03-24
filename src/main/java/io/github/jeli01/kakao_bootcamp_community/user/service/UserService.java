package io.github.jeli01.kakao_bootcamp_community.user.service;

import io.github.jeli01.kakao_bootcamp_community.board.domain.Board;
import io.github.jeli01.kakao_bootcamp_community.board.repository.BoardRepository;
import io.github.jeli01.kakao_bootcamp_community.cloud.s3.FileUtils;
import io.github.jeli01.kakao_bootcamp_community.comment.domain.Comment;
import io.github.jeli01.kakao_bootcamp_community.comment.repository.CommentRepository;
import io.github.jeli01.kakao_bootcamp_community.like.domain.Like;
import io.github.jeli01.kakao_bootcamp_community.like.repository.LikeRepository;
import io.github.jeli01.kakao_bootcamp_community.user.domain.User;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PatchPasswordRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PatchUserBasicRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.request.PostSignUpRequest;
import io.github.jeli01.kakao_bootcamp_community.user.dto.response.PatchPasswordResponse;
import io.github.jeli01.kakao_bootcamp_community.user.exception.NicknameDuplicatedException;
import io.github.jeli01.kakao_bootcamp_community.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
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
    private final FileUtils fileUtils;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    public void signUp(PostSignUpRequest req) {
        validateEmailDuplication(req.getEmail());
        validateNicknameDuplication(req.getNickname());

        String imagePath = storeProfileImage(req.getProfileImage());

        User user = new User(req.getEmail(), bCryptPasswordEncoder.encode(req.getPassword()),
                req.getNickname(), imagePath, "ROLE_USER", LocalDateTime.now(), LocalDateTime.now(), null);

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getActiveUserById(id);

        cascadeCommentDeleteFromUser(user);
        cascadeLikeDeleteFromUser(user);
        cascadeBoardDeleteFromUser(user);

        user.delete();
    }

    private void cascadeBoardDeleteFromUser(User user) {
        List<Board> boards = boardRepository.findByWriterIdAndDeleteDateIsNull(user.getId());
        for (Board board : boards) {
            cacadeCommentDeleteFromBoard(board);
            cascadeLikeDeleteFromBoard(board);

            board.softDelete();
        }
    }

    private void cascadeLikeDeleteFromBoard(Board board) {
        List<Like> boardLikes = likeRepository.findByBoardAndDeleteDateIsNull(board);
        for (Like like : boardLikes) {
            like.softDelete();
            board.minusLikeCount();
        }
    }

    private void cacadeCommentDeleteFromBoard(Board board) {
        List<Comment> boardComments = commentRepository.findByBoardIdAndDeleteDateIsNull(board.getId());
        for (Comment comment : boardComments) {
            comment.softDelete();
            board.minusCommentCount();
        }
    }

    private void cascadeLikeDeleteFromUser(User user) {
        List<Like> likes = likeRepository.findByUserAndDeleteDateIsNull(user);
        for (Like like : likes) {
            like.softDelete();
            like.getBoard().minusLikeCount();
        }
    }

    private void cascadeCommentDeleteFromUser(User user) {
        List<Comment> comments = commentRepository.findByWriterIdAndDeleteDateIsNull(user.getId());
        for (Comment comment : comments) {
            comment.softDelete();
            comment.getBoard().minusCommentCount();
        }
    }

    public void patchUserBasic(PatchUserBasicRequest request, Long id) {
        User user = getActiveUserById(id);

        String newImagePath = user.getProfileImage();
        if (request.getProfileImage() != null) {
            fileUtils.deleteFile(user.getProfileImage());
            newImagePath = storeProfileImage(request.getProfileImage());
        }

        if (!request.getNickname().equals(user.getNickname())) {
            validateNicknameDuplication(request.getNickname());
        }

        user.changeUserBasic(newImagePath, request.getNickname());
    }

    private User getActiveUserById(Long id) {
        return userRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("활성화된 유저를 찾을 수 없습니다. ID: " + id));
    }

    public PatchPasswordResponse patchUserPassword(PatchPasswordRequest patchPasswordRequest, Long id) {
        User user = userRepository.findByIdAndDeleteDateIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
        user.changePassword(bCryptPasswordEncoder.encode(patchPasswordRequest.getPassword()));
        return new PatchPasswordResponse();
    }

    private void validateEmailDuplication(String email) {
        if (userRepository.existsByEmailAndDeleteDateIsNull(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    private void validateNicknameDuplication(String nickname) {
        if (userRepository.existsByNicknameAndDeleteDateIsNull(nickname)) {
            throw new NicknameDuplicatedException("이미 존재하는 닉네임입니다.");
        }
    }

    private String storeProfileImage(MultipartFile profileImage) {
        return profileImage != null ? fileUtils.storeFile(profileImage) : null;
    }

    public User findByEmailAndDeleteDateIsNull(String email) {
        return userRepository.findByEmailAndDeleteDateIsNull(email).orElseThrow(() ->
                new IllegalArgumentException("no user by exists email"));
    }

    public Boolean existsByIdAndDeleteDateIsNull(Long id) {
        return userRepository.existsByIdAndDeleteDateIsNull(id);
    }
}
