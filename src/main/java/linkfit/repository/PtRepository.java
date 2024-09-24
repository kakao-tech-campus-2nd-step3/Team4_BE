package linkfit.repository;

import java.util.Optional;
import linkfit.entity.Pt;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PtRepository extends JpaRepository<Pt, Long> {
    Page<Pt> findAllByTrainer(Trainer trainer, Pageable pageable);
    Optional<Pt> findByUser(User user);
}
