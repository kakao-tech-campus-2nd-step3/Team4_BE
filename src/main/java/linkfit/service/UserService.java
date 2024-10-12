package linkfit.service;

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
import linkfit.exception.PermissionException;
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

    public UserService(UserRepository userRepository, BodyInfoRepository bodyInfoRepository,
        JwtUtil jwtUtil, ImageUploadService imageUploadService) {
        this.userRepository = userRepository;
        this.bodyInfoRepository = bodyInfoRepository;
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
            .orElseThrow(() -> new NotFoundException("not.found.user"));
    }

    public BodyInfo getBodyInfo(Long id) {
        return bodyInfoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not.found.bodyinfo"));
    }

    public void deleteBodyInfo(Long userId, Long bodyInfoId) {
        BodyInfo bodyInfo = getBodyInfo(bodyInfoId);
        validateBodyInfoOwnership(bodyInfo, userId);
        bodyInfoRepository.delete(bodyInfo);
    }

    private void validateBodyInfoOwnership(BodyInfo bodyInfo, Long userId) {
        Long ownerId = bodyInfo.getUser().getId();
        if(!Objects.equals(ownerId, userId)) {
            throw new PermissionException("bodyinfo.permission.denied");
        }
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
        if(imageUrl != null) {
            user.setProfileImageUrl(imageUrl);
        }
    }
}