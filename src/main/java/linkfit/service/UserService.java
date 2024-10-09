package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.DUPLICATE_EMAIL;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_BODYINFO;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_USER;

import java.util.List;
import java.util.Objects;
import linkfit.dto.BodyInfoResponse;
import linkfit.dto.LoginRequest;
import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;
import linkfit.dto.UserRegisterRequest;
import linkfit.entity.BodyInfo;
import linkfit.entity.User;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.repository.BodyInfoRepository;
import linkfit.repository.UserRepository;
import linkfit.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BodyInfoRepository bodyInfoRepository;
    private final JwtUtil jwtUtil;
    private final ImageUploadService imageUploadService;

    public UserService(
        UserRepository userRepository,
        BodyInfoRepository bodyInfoRepository,
        JwtUtil jwtUtil,
        ImageUploadService imageUploadService) {
        this.userRepository = userRepository;
        this.bodyInfoRepository = bodyInfoRepository;
        this.jwtUtil = jwtUtil;
        this.imageUploadService = imageUploadService;
    }

    @Transactional
    public void register(UserRegisterRequest request, MultipartFile profileImage) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateException(DUPLICATE_EMAIL);
        }
        User user = request.toEntity();
        String imageUrl = imageUploadService.uploadProfileImage(profileImage);
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        user.validatePassword(request.password());
        return jwtUtil.generateToken(user.getId(), user.getEmail());
    }

    public UserProfileResponse getProfile(Long userId) {
        User user = getUser(userId);
        return user.toDto();
    }

    public void updateProfile(Long userId, UserProfileRequest request,
        MultipartFile profileImage) {
        User user = getUser(userId);
        String imageUrl = imageUploadService.uploadProfileImage(profileImage);
        user.setProfileImageUrl(imageUrl);
        user.update(request);
        userRepository.save(user);
    }

    public void registerBodyInfo(Long userId, MultipartFile profileImage) {
        User user = getUser(userId);
        String imageUrl = imageUploadService.saveImage(profileImage);
        BodyInfo bodyInfo = new BodyInfo(user, imageUrl);
        bodyInfoRepository.save(bodyInfo);

    }

    public List<BodyInfoResponse> getAllBodyInfo(Long userId, Pageable pageable) {
        User user = getUser(userId);
        Page<BodyInfo> bodyInfos = bodyInfoRepository.findAllByUserId(user.getId(), pageable);
        return bodyInfos.stream()
            .map(BodyInfo::toDto)
            .toList();
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }

    public BodyInfo getUserBodyInfo(Long userId) {
        return bodyInfoRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_BODYINFO));
    }

    public BodyInfo getRecentBodyInfo(Long userId) {
        return bodyInfoRepository.findFirstByUserIdOrderByCreateDateDesc(userId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_BODYINFO));
    }

    public void deleteBodyInfo(Long userId, Long infoId) {
        User user = getUser(userId);
        BodyInfo bodyInfo = bodyInfoRepository.findById(infoId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_BODYINFO));

        if (Objects.equals(user.getId(), bodyInfo.getUser().getId())) {
            bodyInfoRepository.delete(bodyInfo);
        }

    }
}
