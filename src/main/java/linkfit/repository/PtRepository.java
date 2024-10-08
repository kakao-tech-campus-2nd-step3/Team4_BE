package linkfit.repository;

import java.util.List;
import java.util.Optional;
import linkfit.entity.Pt;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.status.PtStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PtRepository extends JpaRepository<Pt, Long> {

    Page<Pt> findAllByTrainer(Trainer trainer, Pageable pageable);

    List<Pt> findByUser(User user);

    Optional<Pt> findByUserAndStatus(User user, PtStatus status);

    Page<Pt> findAllByUserAndStatus(User user, PtStatus status, Pageable pageable);
}
