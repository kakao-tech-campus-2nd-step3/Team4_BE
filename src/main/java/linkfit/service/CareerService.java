package linkfit.service;

import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.entity.Career;
import linkfit.entity.Trainer;
import linkfit.exception.NotFoundCareerException;
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
        return careers.stream().map(CareerResponse::new).toList();
    }

    public void addCareer(Trainer trainer, CareerRequest req) {
        Career career = new Career(trainer, req.getCareer());
        careerRepository.save(career);
    }

    public void deleteCareer(Long careerId) {
        careerRepository.findById(careerId)
            .orElseThrow(() -> new NotFoundCareerException("Career not found"));

        careerRepository.deleteById(careerId);
    }

    public Long findTrainerIdByCarreerId(Long careerId) {
        Career career = careerRepository.findById(careerId)
            .orElseThrow(() -> new NotFoundCareerException("This is a career that doesn’t exist."));
        return career.getTrainer().getId();
    }
}