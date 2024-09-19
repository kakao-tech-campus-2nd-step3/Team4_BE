package linkfit.service;

import org.springframework.stereotype.Service;

import linkfit.entity.Trainer;
import linkfit.repository.TrainerRepository;

@Service
public class TrainerService extends PersonService<Trainer> {

    public TrainerService(TrainerRepository trainerRepository) {
        super(trainerRepository);
    }
}
