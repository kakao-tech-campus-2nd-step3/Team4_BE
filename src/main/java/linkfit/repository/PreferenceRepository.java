package linkfit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import linkfit.entity.Preference;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {

	Optional<Preference> getByUserId(Long userId);
}