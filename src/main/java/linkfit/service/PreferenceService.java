package linkfit.service;

import org.springframework.stereotype.Service;

import linkfit.dto.PreferenceRequest;
import linkfit.entity.Preference;
import linkfit.entity.User;
import linkfit.entity.UserBodyInfo;
import linkfit.repository.PreferenceRepository;

@Service
public class PreferenceService {

	private PreferenceRepository preferenceRepository;
	private UserService userService;
	
	public PreferenceService(PreferenceRepository preferenceRepository, UserService userService) {
		this.preferenceRepository = preferenceRepository;
		this.userService = userService;
	}
	
	public void registerPreference(String authorization, PreferenceRequest request) {
		Preference preference = request.toEntity();
		User user = userService.getUser(authorization);
		UserBodyInfo userBodyInfo = userService.getUserBodyInfo(user.getId());
		preference.setUserBodyInfo(userBodyInfo);
		preferenceRepository.save(preference);
	}
}
