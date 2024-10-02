package linkfit.repository;

import linkfit.entity.Gym;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
    Page<Gym> findAllByNameContaining(String keyword, Pageable pageable);
}