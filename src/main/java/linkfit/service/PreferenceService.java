package linkfit.service;

import java.util.ArrayList;
import java.util.List;

import linkfit.entity.Sports;
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
        //최신 BodyInfo를 받아오는 메서드로 수정 필요
        BodyInfo bodyInfo = userService.getRecentBodyInfo(user.getId());
        Sports sports = sportsService.getSportsById(request.sportsId());
        Preference preference = request.toEntity(bodyInfo, sports);
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
        String userLocation = preference.getBodyInfo().getUser().getLocation();
        Coordinate userCoordinates = distanceCalculatorService.getCoordinates(userLocation);
        double distance = distanceCalculatorService.calculateHaversineDistance(gymCoordinates,
            userCoordinates);
        if (preference.getRange() >= distance) {
            response.add(preference.toDto());
        }
    }
}
