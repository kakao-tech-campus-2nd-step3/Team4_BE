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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BodyInfoRepository bodyInfoRepository;
    private final JwtUtil jwtUtil;
    private final ImageUploadService imageUploadService;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, BodyInfoRepository bodyInfoRepository,
        JwtUtil jwtUtil, ImageUploadService imageUploadService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bodyInfoRepository = bodyInfoRepository;
        this.jwtUtil = jwtUtil;
        this.imageUploadService = imageUploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(UserRegisterRequest request, MultipartFile profileImage) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateException("duplicate.email");
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = request.toEntity(encodedPassword);
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = imageUploadService.uploadProfileImage(profileImage);
            user.setProfileImageUrl(imageUrl);
        }
        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new NotFoundException("not.found.user"));
        if(!userAuthenticate(user, request.password())){
            throw new PermissionException("not.match.password");
        }
        return jwtUtil.generateToken(user.getId(), user.getEmail());
    }

    public UserProfileResponse getProfile(Long userId) {
        User user = getUser(userId);
        return user.toDto();
    }

    public void updateProfile(Long userId, UserProfileRequest request,
        MultipartFile profileImage) {
        User user = getUser(userId);
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = imageUploadService.uploadProfileImage(profileImage);
            user.setProfileImageUrl(imageUrl);
        }
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
            .orElseThrow(() -> new NotFoundException("not.found.user"));
    }

    public BodyInfo getUserBodyInfo(Long userId) {
        return bodyInfoRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("not.found.bodyinfo"));
    }

    public BodyInfo getRecentBodyInfo(Long userId) {
        return bodyInfoRepository.findFirstByUserIdOrderByCreateDateDesc(userId)
            .orElseThrow(() -> new NotFoundException("not.found.bodyinfo"));
    }

    public void deleteBodyInfo(Long userId, Long infoId) {
        User user = getUser(userId);
        BodyInfo bodyInfo = bodyInfoRepository.findById(infoId)
            .orElseThrow(() -> new NotFoundException("not.found.bodyinfo"));

        if (Objects.equals(user.getId(), bodyInfo.getUser().getId())) {
            bodyInfoRepository.delete(bodyInfo);
        }

    }

    private boolean userAuthenticate(User user, String rawPassword) {
       return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
