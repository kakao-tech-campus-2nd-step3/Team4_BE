package linkfit.repository;

import java.util.List;
import linkfit.entity.Gym;
import linkfit.entity.GymImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymImageRepository extends JpaRepository<GymImage, Long> {
    List<GymImage> findAllByGym(Gym gym);
    void deleteAllByGym(Gym gym);
}
