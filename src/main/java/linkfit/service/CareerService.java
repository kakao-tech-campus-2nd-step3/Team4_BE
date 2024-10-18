package linkfit.service;

import java.util.List;
import java.util.Objects;
import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.entity.Career;
import linkfit.entity.Trainer;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.CareerRepository;
import linkfit.repository.TrainerRepository;
import org.springframework.stereotype.Service;

@Service
public class CareerService {

    private final CareerRepository careerRepository;
    private final TrainerRepository trainerRepository;

    public CareerService(CareerRepository careerRepository, TrainerRepository trainerRepository) {
        this.careerRepository = careerRepository;
        this.trainerRepository = trainerRepository;
    }

    public List<CareerResponse> getAllCareers(Long trainerId) {
        Trainer trainer = getTrainer(trainerId);
        List<Career> careers = careerRepository.findAllByTrainer(trainer);
        return careers.stream()
            .map(Career::toDto)
            .toList();
    }

    public void addCareer(Long trainerId, List<CareerRequest> request) {
        Trainer trainer = getTrainer(trainerId);
        request.forEach(c -> {
            Career career = new Career(trainer, c.career());
            careerRepository.save(career);
        });
    }

    public void deleteCareer(Long trainerId, Long careerId) {
        validateCareerOwnership(careerId, trainerId);
        careerRepository.deleteById(careerId);
    }

    private Career getCareer(Long careerId) {
        return careerRepository.findById(careerId)
            .orElseThrow(() -> new NotFoundException("not.found.career"));
    }

    private Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId).orElseThrow(
            () -> new NotFoundException("not.found.trainer")
        );
    }

    private void validateCareerOwnership(Long careerId, Long trainerId) {
        getTrainer(trainerId);
        Career career = getCareer(careerId);
        Long ownerId = career.getTrainer().getId();
        if (!Objects.equals(ownerId, trainerId)) {
            throw new PermissionException("career.permission.denied");
        }
    }
}