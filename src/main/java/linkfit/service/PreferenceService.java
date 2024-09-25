package linkfit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import linkfit.dto.Coordinate;
import linkfit.dto.PreferenceRequest;
import linkfit.dto.PreferenceResponse;
import linkfit.entity.BodyInfo;
import linkfit.entity.Preference;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.repository.PreferenceRepository;

@Service
public class PreferenceService {

	private PreferenceRepository preferenceRepository;
	private UserService userService;
	private TrainerService trainerService;
	private DistanceCalculatorService distanceCalculatorService;
	
	public PreferenceService(PreferenceRepository preferenceRepository, UserService userService,
			TrainerService trainerService, DistanceCalculatorService distanceCalculatorService) {
		this.preferenceRepository = preferenceRepository;
		this.userService = userService;
		this.trainerService = trainerService;
		this.distanceCalculatorService = distanceCalculatorService;
	}
	
	public void registerPreference(String authorization, PreferenceRequest request) {
		Preference preference = request.toEntity();
		User user = userService.getUser(authorization);
		BodyInfo userBodyInfo = userService.getUserBodyInfo(user.getId());
		preference.setUserBodyInfo(userBodyInfo);
		preference.setSportsType(request.sportsType());
		preferenceRepository.save(preference);
	}
	
	public List<PreferenceResponse> getAllPreference(String authorization) {
	    Trainer trainer = trainerService.getTrainer(authorization);
	    String gymLocation = trainer.getGym().getGymLocation();

	    List<Preference> preferences = preferenceRepository.findAll();
	    List<PreferenceResponse> response = new ArrayList<>();

	    Coordinate gymCoordinates = distanceCalculatorService.getCoordinates(gymLocation);

	    for (Preference preference : preferences) {
	        processPreference(preference, gymCoordinates, response);
	    }

	    return response;
	}
	
	private void processPreference(Preference preference, Coordinate gymCoordinates, List<PreferenceResponse> response) {
	    String userLocation = preference.getBodyInfo().getUser().getLocation();
	    Coordinate userCoordinates = distanceCalculatorService.getCoordinates(userLocation);
	    
	    double distance = distanceCalculatorService.calculateHaversineDistance(gymCoordinates, userCoordinates);
	    
	    if (preference.getRange() >= distance) {
	        response.add(preference.toDto());
	    }
	}
}
