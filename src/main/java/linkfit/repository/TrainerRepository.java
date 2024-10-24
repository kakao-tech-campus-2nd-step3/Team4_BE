package linkfit.repository;

import java.util.List;
import java.util.Optional;
import linkfit.entity.Gym;
import linkfit.entity.Trainer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    boolean existsByEmail(String email);

    Optional<Trainer> findByEmail(String email);

    List<Trainer> findAllByGym(Gym gym, Pageable pageable);
}
