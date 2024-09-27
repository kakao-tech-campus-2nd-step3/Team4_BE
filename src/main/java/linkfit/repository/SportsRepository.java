package linkfit.repository;

import linkfit.entity.Sports;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SportsRepository extends JpaRepository<Sports, Long> {
    boolean existsByName(String name);
}