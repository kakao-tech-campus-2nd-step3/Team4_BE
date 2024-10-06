package linkfit.repository;

import java.util.Optional;
import linkfit.entity.Gym;
import linkfit.entity.GymAdminRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymAdminRelationRepository extends JpaRepository<GymAdminRelation, Long> {

    Optional<GymAdminRelation> deleteByGym(Gym gym);

    Optional<GymAdminRelation> findByGym(Gym gym);
}
