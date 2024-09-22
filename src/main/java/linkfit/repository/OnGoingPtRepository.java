package linkfit.repository;

import java.util.Optional;
import linkfit.entity.OnGoingPt;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnGoingPtRepository extends JpaRepository<OnGoingPt, Long> {
    Page<OnGoingPt> findAllByTrainer(Trainer trainer, Pageable pageable);
    Optional<OnGoingPt> findByUser(User user);
}
