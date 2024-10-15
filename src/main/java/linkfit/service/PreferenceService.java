package linkfit.service;

import java.util.List;

import linkfit.dto.Coordinate;
import linkfit.dto.PreferenceRequest;
import linkfit.dto.PreferenceResponse;
import linkfit.entity.BodyInfo;
import linkfit.entity.Gym;
import linkfit.entity.Preference;
import linkfit.entity.Sports;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.BodyInfoRepository;
import linkfit.repository.PreferenceRepository;
import linkfit.status.TrainerGender;

import org.springframework.stereotype.Service;

@Service
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final UserService userService;
    private final TrainerService trainerService;
    private final DistanceCalculatorService distanceCalculatorService;
    private final SportsService sportsService;
    private final BodyInfoRepository bodyInfoRepository;

    public PreferenceService(PreferenceRepository preferenceRepository, UserService userService,
        TrainerService trainerService, DistanceCalculatorService distanceCalculatorService,
        SportsService sportsService, BodyInfoRepository bodyInfoRepository) {
        this.preferenceRepository = preferenceRepository;
        this.userService = userService;
        this.trainerService = trainerService;
        this.distanceCalculatorService = distanceCalculatorService;
        this.sportsService = sportsService;
        this.bodyInfoRepository = bodyInfoRepository;
    }

    public void registerPreference(Long userId, PreferenceRequest request) {
        User user = userService.getUser(userId);
        Sports sports = sportsService.getSportsById(request.sportsId());
        BodyInfo bodyInfo = getLastBodyInfo(user);
        Preference preference = request.toEntity(user, bodyInfo, sports);
        preferenceRepository.save(preference);
    }

    private BodyInfo getLastBodyInfo(User user) {
        return bodyInfoRepository.findTopByUserOrderByCreateDate(user)
            .orElseThrow(() -> new NotFoundException("not.found.bodyinfo"));
    }

    public List<PreferenceResponse> getAllMatchingPossible(Long trainerId) {
        Trainer trainer = trainerService.getTrainer(trainerId);
        List<Preference> preferences = preferenceRepository.findAll();
        System.out.println(preferences.toString());
        validGender(preferences, trainer.getGender());
        System.out.println(preferences.toString());
        validDistance(preferences, trainer.getGym());
        System.out.println(preferences.toString());
        return preferences.stream()
            .map(Preference::toDto)
            .toList();
    }

    private void validGender(List<Preference> preferences, TrainerGender gender) {
        preferences.removeIf(preference -> !preference.isValidTrainerGender(gender));
    }

    private void validDistance(List<Preference> preferences, Gym gym) {
        String gymLocation = gym.getLocation();
        Coordinate gymCoordinates = distanceCalculatorService.getCoordinates(gymLocation);
        preferences.removeIf(preference -> !isWithinDistance(preference, gymCoordinates));
    }

    private boolean isWithinDistance(Preference preference, Coordinate gymCoordinates) {
        User user = preference.getUser();
        String userLocation = user.getLocation();
        Coordinate userCoordinates = distanceCalculatorService.getCoordinates(userLocation);
        double distance = distanceCalculatorService.calculateHaversineDistance(gymCoordinates,
            userCoordinates);
        return preference.getRange() >= distance;
    }

    public void deletePreference(Long preferenceId, Long userId) {
        User user = userService.getUser(userId);
        Preference preference = preferenceRepository.findById(preferenceId)
            .orElseThrow(() -> new NotFoundException("not.found.preference"));
        if (!user.equals(preference.getUser())) {
            throw new PermissionException("not.owner");
        }
        preferenceRepository.delete(preference);
    }
}
