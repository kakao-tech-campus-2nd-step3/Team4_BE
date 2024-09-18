package linkfit.repository;

import linkfit.entity.Career;
import linkfit.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TrainerRepository extends JpaRepository<Trainer,Long> {

}