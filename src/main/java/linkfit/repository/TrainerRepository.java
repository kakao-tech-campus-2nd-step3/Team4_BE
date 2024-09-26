package linkfit.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import linkfit.entity.Trainer;

@Repository
public interface TrainerRepository extends PersonRepository<Trainer> {

    Optional<Trainer> findByEmail(String email);
}
