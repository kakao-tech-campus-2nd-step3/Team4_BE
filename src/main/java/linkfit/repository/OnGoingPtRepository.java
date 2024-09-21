package linkfit.repository;

import linkfit.entity.OnGoingPt;
import linkfit.entity.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnGoingPtRepository extends JpaRepository<OnGoingPt, Long> {

    Page<OnGoingPt> findAllByTrainer(Trainer trainer, Pageable pageable);
}
