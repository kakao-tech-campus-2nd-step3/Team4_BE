package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_BODYINFO;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_USER;

import java.util.List;
import linkfit.dto.UserBodyInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;
import linkfit.entity.User;
import linkfit.entity.BodyInfo;
import linkfit.exception.NotFoundException;
import linkfit.repository.BodyInfoRepository;
import linkfit.repository.UserRepository;
import linkfit.util.JwtUtil;

@Service
public class UserService extends PersonService<User> {

    private UserRepository userRepository;
    private final BodyInfoRepository bodyInfoRepository;
    private final JwtUtil jwtUtil;
    private final ImageUploadService imageUploadService;


    public UserService(
        UserRepository userRepository,
        BodyInfoRepository bodyInfoRepository,
        JwtUtil jwtUtil,
        ImageUploadService imageUploadService) {
        super(userRepository);
        this.bodyInfoRepository = bodyInfoRepository;
        this.jwtUtil = jwtUtil;
        this.imageUploadService = imageUploadService;
    }

    public UserProfileResponse getProfile(String authorization) {
        User user = getUser(authorization);
        return user.toDto();
    }

    public void updateProfile(
        String authorization,
        UserProfileRequest request,
        MultipartFile profileImage) {
        User user = getUser(authorization);
        imageUploadService.saveProfileImage(user, profileImage);
        user.update(request);
        userRepository.save(user);
    }

    public void registerBodyInfo(String authorization, MultipartFile profileImage) {
        User user = getUser(authorization);
        imageUploadService.saveProfileImage(user, profileImage);
    }

    public List<UserBodyInfoResponse> getAllBodyInfo(String authorization, Pageable pageable) {
        User user = getUser(authorization);
        Page<BodyInfo> bodyInfos = bodyInfoRepository.findAllByUserId(user.getId(), pageable);
        return bodyInfos.stream()
            .map(BodyInfo::toDto)
            .toList();
    }

    public User getUser(String authorization) {
        Long userId = jwtUtil.parseToken(authorization);
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }

    public BodyInfo getUserBodyInfo(Long userId) {
        return bodyInfoRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_BODYINFO));
    }
}
