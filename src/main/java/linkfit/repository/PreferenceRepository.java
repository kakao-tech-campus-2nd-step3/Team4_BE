package linkfit.repository;

import java.util.Optional;
import linkfit.entity.Preference;
import linkfit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    Optional<Preference> findByUser(User user);
    void deleteByUser(User user);
}