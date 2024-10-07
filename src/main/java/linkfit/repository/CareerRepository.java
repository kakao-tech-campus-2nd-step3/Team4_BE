package linkfit.repository;

import java.util.List;

import linkfit.entity.Career;
import linkfit.entity.Trainer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {

    List<Career> findAllByTrainer(Trainer trainer);
}