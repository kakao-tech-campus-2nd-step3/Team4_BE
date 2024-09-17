package linkfit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import linkfit.entity.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long>{

	
}
