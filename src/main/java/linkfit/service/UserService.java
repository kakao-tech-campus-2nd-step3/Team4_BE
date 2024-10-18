package linkfit.service;

import linkfit.dto.LoginRequest;
import linkfit.dto.TokenResponse;
import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;
import linkfit.dto.UserRegisterRequest;
import linkfit.entity.User;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.UserRepository;
import linkfit.status.Role;
import linkfit.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ImageUploadService imageUploadService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil,
        ImageUploadService imageUploadService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.imageUploadService = imageUploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(UserRegisterRequest request) {
        validateEmailAlreadyExist(request.email());
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = request.toEntity(encodedPassword);
        userRepository.save(user);
    }

    public TokenResponse login(LoginRequest request) {
        User user = getUserByEmail(request.email());
        if (!userAuthenticate(user, request.password())) {
            throw new PermissionException("not.match.password");
        }
        return new TokenResponse(jwtUtil.generateToken(Role.USER, user.getId(), user.getEmail()));
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

    private boolean userAuthenticate(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    private void handleProfileImage(MultipartFile profileImage, User user) {
        String imageUrl = imageUploadService.uploadProfileImage(profileImage);
        if (imageUrl != null) {
            user.setProfileImageUrl(imageUrl);
        }
    }

}

