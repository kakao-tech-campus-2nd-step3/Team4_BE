package linkfit.repository;

import java.util.List;
import linkfit.entity.Gym;

import linkfit.status.GymStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {

    Page<Gym> findAllByNameContainingAndStatus(String keyword, GymStatus status, Pageable pageable);

    List<Gym> findAllByStatus(GymStatus status);

    Page<Gym> findAllByStatus(GymStatus status, Pageable pageable);
}