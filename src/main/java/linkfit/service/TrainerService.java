package linkfit.service;

import linkfit.dto.TrainerProfileResponse;
import linkfit.entity.Trainer;
import linkfit.repository.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;

    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public TrainerProfileResponse getProfile(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
            .orElseThrow(() -> new IllegalStateException("존재하지 않는 트레이너."));
        return new TrainerProfileResponse(trainer);
    }


}