package linkfit.repository;

import linkfit.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Long> {

    List<Career> findAllByTrainerId(Long id);
}