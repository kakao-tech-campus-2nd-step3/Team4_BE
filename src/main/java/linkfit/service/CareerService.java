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

    public void addCareer(Trainer trainer, List<CareerRequest> request) {

        request.forEach(c -> {
            Career career = new Career(trainer, c.career());
            careerRepository.save(career);
        });

    }

    public void deleteCareer(Long careerId) {
        if (careerRepository.existsById(careerId)) {
            throw new NotFoundException(NOT_FOUND_CAREER);
        }
        careerRepository.deleteById(careerId);
    }

    public Career getCareer(Long careerId) {
        return careerRepository.findById(careerId).
            orElseThrow(() -> new NotFoundException(NOT_FOUND_CAREER));
    }
}