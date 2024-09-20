package linkfit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;
import linkfit.entity.User;
import linkfit.entity.UserBodyInfo;
import linkfit.exception.InvalidIdException;
import linkfit.repository.UserBodyInfoRepository;
import linkfit.repository.UserRepository;
import linkfit.util.JwtUtil;

@Service
public class UserService extends PersonService<User> {

	private UserRepository userRepository;
	private final UserBodyInfoRepository userBodyInfoRepository;
	private final JwtUtil jwtUtil;
	private final ImageUploadService imageUploadService;
	
	
    public UserService(UserRepository userRepository, UserBodyInfoRepository userBodyInfoRepository,
    		JwtUtil jwtUtil, ImageUploadService imageUploadService) {
        super(userRepository);
        this.userBodyInfoRepository = userBodyInfoRepository;
        this.jwtUtil = jwtUtil;
        this.imageUploadService = imageUploadService;
    }
    
    public UserProfileResponse getProfile(String authorization) {
        User user = getUser(authorization);
        UserProfileResponse response = user.toDto();
        return response;
    }

    public void updateProfile(String authorization, UserProfileRequest request,
    		MultipartFile profileImage) {
    	User user = getUser(authorization);
        User newUser = user.Update(request);
        userRepository.save(newUser);
    }

    public void registerBodyInfo(String authorization, MultipartFile profileImage) {
    	User user = getUser(authorization);
    	imageUploadService.profileImageSave(user, profileImage);
    }

    public Page<UserBodyInfo> getAllBodyInfo(String authorization, Pageable pageable) {
    	User user = getUser(authorization);
        return userBodyInfoRepository.findAllByUserId(user.getId(), pageable);
    }
    
    public User getUser(String authorization) {
    	Long userId = jwtUtil.parseToken(authorization);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidIdException("User profile does not exist."));   
        return user;
    }
}
