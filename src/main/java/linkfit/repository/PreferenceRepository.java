package linkfit.repository;

import linkfit.entity.Preference;
import linkfit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    void deleteByUser(User user);
}