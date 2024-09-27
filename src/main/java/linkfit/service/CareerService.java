package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_CAREER;

import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.entity.Career;
import linkfit.entity.Trainer;
import linkfit.exception.NotFoundException;
import linkfit.repository.CareerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerService {

    private final CareerRepository careerRepository;

    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public List<CareerResponse> getAllTrainerCareers(Long trainerId) {
        List<Career> careers = careerRepository.findAllByTrainerId(trainerId);
        return careers.stream()
            .map(Career::toDto)
            .toList();
    }

    public void addCareer(Trainer trainer, CareerRequest req) {
        Career career = new Career(trainer, req.career());
        careerRepository.save(career);
    }

    public void deleteCareer(Long careerId) {
        careerRepository.findById(careerId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_CAREER));
        careerRepository.deleteById(careerId);
    }

    public Long findTrainerIdByCareerId(Long careerId) {
        Career career = careerRepository.findById(careerId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_CAREER));
        return career.getTrainer().getId();
    }
}