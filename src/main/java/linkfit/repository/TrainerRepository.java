package linkfit.repository;

import org.springframework.stereotype.Repository;

import linkfit.entity.Trainer;

@Repository("trainerRepository")
public interface TrainerRepository extends PersonRepository<Trainer> {

	
}
