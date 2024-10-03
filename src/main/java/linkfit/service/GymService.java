package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_GYM;

import java.util.List;
import linkfit.dto.GymDetailResponse;
import linkfit.dto.GymLocationResponse;
import linkfit.dto.GymRegisterRequest;
import linkfit.dto.GymSearchResponse;
import linkfit.dto.GymTrainersResponse;
import linkfit.entity.Gym;
import linkfit.entity.GymImage;
import linkfit.entity.Trainer;
import linkfit.exception.NotFoundException;
import linkfit.repository.GymImageRepository;
import linkfit.repository.GymRepository;
import linkfit.repository.TrainerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GymService {

    private final GymRepository gymRepository;
    private final TrainerRepository trainerRepository;
    private final GymImageRepository gymImageRepository;

    public GymService(GymRepository gymRepository, TrainerRepository trainerRepository,
        GymImageRepository gymImageRepository) {
        this.gymRepository = gymRepository;
        this.trainerRepository = trainerRepository;
        this.gymImageRepository = gymImageRepository;
    }

    public List<Gym> findAllGym(Pageable pageable) {
        return gymRepository.findAll(pageable).stream().toList();
    }

    public void registerGym(GymRegisterRequest gymRegisterRequest) {
        gymRepository.save(gymRegisterRequest.toEntity());
    }

    public void deleteGym(Long gymId) {
        if (!gymRepository.existsById(gymId)) {
            throw new NotFoundException(NOT_FOUND_GYM);
        }
        gymRepository.deleteById(gymId);
    }

    public GymSearchResponse findAllByKeyword(String keyword, Pageable pageable) {
        Page<Gym> gymList = gymRepository.findAllByNameContaining(keyword, pageable);
        return new GymSearchResponse(gymList.getContent());
    }

    public GymDetailResponse getGymDetails(Long gymId) {
        Gym gym = getGymById(gymId);
        List<GymImage> images = gymImageRepository.findAllByGym(gym);
        return new GymDetailResponse(gym, images);
    }

    public Gym getGymById(Long id) {
        return gymRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_GYM));
    }

    public GymTrainersResponse getGymTrainers(Long gymId, Pageable pageable) {
        Gym gym = getGymById(gymId);
        List<Trainer> trainerList = trainerRepository.findAllByGym(gym, pageable);
        return new GymTrainersResponse(trainerList);
    }

    public List<GymLocationResponse> getGymLocations() {
        List<Gym> gymList = gymRepository.findAll();
        return gymList.stream()
            .map(gym -> new GymLocationResponse(gym.getId(), gym.getLocation()))  // Assuming GymLocationResponse has these fields
            .toList();
    }
}
