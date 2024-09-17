package linkfit.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import linkfit.dto.UserRegisterRequest;
import linkfit.entity.User;
import linkfit.repository.TrainerRepository;

@Service
public class AuthService {

	private final TrainerRepository trainerRepository;
	private final UserService userService;
	private final ImageUploadService imageUploadService;
	
	public AuthService(TrainerRepository trainerRepository, UserService userService,
			ImageUploadService imageUploadService) {
		this.trainerRepository = trainerRepository;
		this.userService = userService;
		this.imageUploadService = imageUploadService;
	}
	
	public void userRegister(UserRegisterRequest request, MultipartFile profileImage) {
		User user = request.toEntity();
		userService.existsByEmail(request.getEmail());
		request.verifyPassword();
		imageUploadService.profileImageSave(user, profileImage);
		userService.userSave(user);
	}
}
