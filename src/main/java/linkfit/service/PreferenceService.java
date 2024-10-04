package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_PREFERENCE;

import java.util.ArrayList;
import java.util.List;
import linkfit.dto.Coordinate;
import linkfit.dto.PreferenceRequest;
import linkfit.dto.PreferenceResponse;
import linkfit.entity.Preference;
import linkfit.entity.Sports;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.repository.PreferenceRepository;
import org.springframework.stereotype.Service;

@Service
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final UserService userService;
    private final TrainerService trainerService;
    private final DistanceCalculatorService distanceCalculatorService;
    private final SportsService sportsService;

    public PreferenceService(PreferenceRepository preferenceRepository, UserService userService,
        TrainerService trainerService, DistanceCalculatorService distanceCalculatorService,
        SportsService sportsService) {
        this.preferenceRepository = preferenceRepository;
        this.userService = userService;
        this.trainerService = trainerService;
        this.distanceCalculatorService = distanceCalculatorService;
        this.sportsService = sportsService;
    }

    public void registerPreference(String authorization, PreferenceRequest request) {
        User user = userService.getUser(authorization);
        Sports sports = sportsService.getSportsById(request.sportsId());
        Preference preference = request.toEntity(user, sports);
        preferenceRepository.save(preference);
    }


    public List<PreferenceResponse> getAllPreference(String authorization) {
        Trainer trainer = trainerService.getTrainer(authorization);
        String gymLocation = trainer.getGym().getLocation();
        List<Preference> preferences = preferenceRepository.findAll();
        List<PreferenceResponse> response = new ArrayList<>();
        Coordinate gymCoordinates = distanceCalculatorService.getCoordinates(gymLocation);
        for (Preference preference : preferences) {
            processPreference(preference, gymCoordinates, response);
        }
        return response;
    }

    private void processPreference(Preference preference, Coordinate gymCoordinates,
        List<PreferenceResponse> response) {
        User user = preference.getUser();
        String userLocation = user.getLocation();
        Coordinate userCoordinates = distanceCalculatorService.getCoordinates(userLocation);
        double distance = distanceCalculatorService.calculateHaversineDistance(gymCoordinates,
            userCoordinates);
        if (preference.getRange() >= distance) {
            response.add(preference.toDto(userService.getRecentBodyInfo(user.getId())));
        }
    }
    
    public void deletePreference(String authorization) {
    	User user = userService.getUser(authorization);
        Preference preference = preferenceRepository.findByUser(user)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_PREFERENCE));
    	preferenceRepository.delete(preference);
    }
}
