package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_GYM;

import java.util.List;
import linkfit.dto.GymDetailResponse;
import linkfit.dto.GymRegisterRequest;
import linkfit.entity.Gym;
import linkfit.entity.Trainer;
import linkfit.exception.NotFoundException;
import linkfit.repository.GymRepository;
import linkfit.repository.TrainerRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GymService {

    private final GymRepository gymRepository;
    private final TrainerRepository trainerRepository;

    public GymService(GymRepository gymRepository, TrainerRepository trainerRepository) {
        this.gymRepository = gymRepository;
        this.trainerRepository = trainerRepository;
    }

    public List<Gym> findAllGym(Pageable pageable) {
        return gymRepository.findAll(pageable).stream().toList();
    }

    public void registerGym(GymRegisterRequest gymRegisterRequest) {
        gymRepository.save(gymRegisterRequest.toEntity());
    }

    public void deleteGym(Long gymId) {
        if(!gymRepository.existsById(gymId))
            throw new NotFoundException(NOT_FOUND_GYM);
        gymRepository.deleteById(gymId);
    }

    public GymDetailResponse getGymDetails(Long gymId, Pageable pageable) {
        Gym gym = gymRepository.findById(gymId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_GYM));
        List<Trainer> trainerList = trainerRepository.findAllByGym(gym, pageable);
        return new GymDetailResponse(gym, trainerList);
    }
}
