package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_CAREER;

import java.util.List;
import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.entity.Career;
import linkfit.entity.Trainer;
import linkfit.exception.NotFoundException;
import linkfit.repository.CareerRepository;
import org.springframework.stereotype.Service;

@Service
public class CareerService {

    private final CareerRepository careerRepository;

    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public List<CareerResponse> getAllCareerByTrainer(Trainer trainer) {
        List<Career> careers = careerRepository.findAllByTrainer(trainer);
        return careers.stream()
            .map(Career::toDto)
            .toList();
    }

    public void addCareer(Trainer trainer, CareerRequest req) {
        Career career = new Career(trainer, req.career());
        careerRepository.save(career);
    }

    public void deleteCareer(Long careerId) {
        isExistCareer(careerId);
        careerRepository.deleteById(careerId);
    }

    public Trainer getTrainerByCareerId(Long careerId) {
        Career career = isExistCareer(careerId);
        return career.getTrainer();
    }

    private Career isExistCareer(Long careerId) {
        return careerRepository.findById(careerId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_CAREER));
    }
}