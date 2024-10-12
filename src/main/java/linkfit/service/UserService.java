package linkfit.service;

import linkfit.dto.LoginRequest;
import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;
import linkfit.dto.UserRegisterRequest;
import linkfit.entity.User;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.repository.UserRepository;
import linkfit.util.JwtUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ImageUploadService imageUploadService;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil,
        ImageUploadService imageUploadService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.imageUploadService = imageUploadService;
    }

    @Transactional
    public void register(UserRegisterRequest request, MultipartFile profileImage) {
        validateEmailAlreadyExist(request.email());
        User user = request.toEntity();
        handleProfileImage(profileImage, user);
        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = getUserByEmail(request.email());
        user.validatePassword(request.password());
        return jwtUtil.generateToken(user.getId(), user.getEmail());
    }

    public UserProfileResponse getProfile(Long userId) {
        return getUser(userId).toDto();
    }

    public void updateProfile(Long userId, UserProfileRequest request,
        MultipartFile profileImage) {
        User user = getUser(userId);
        user.updateInfo(request);
        handleProfileImage(profileImage, user);
        userRepository.save(user);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("not.found.user"));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("not.found.user"));
    }

    private void validateEmailAlreadyExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateException("duplicate.email");
        }
    }

    private void handleProfileImage(MultipartFile profileImage, User user) {
        String imageUrl = imageUploadService.uploadProfileImage(profileImage);
        if (imageUrl != null) {
            user.setProfileImageUrl(imageUrl);
        }
    }
}