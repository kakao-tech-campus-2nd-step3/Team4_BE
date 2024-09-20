package linkfit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import linkfit.entity.PersonEntity;

@NoRepositoryBean
public interface PersonRepository<T extends PersonEntity> extends JpaRepository<T, Long> {

	boolean existsByEmail(String email);
	
	Optional<T> findByEmail(String email);
}
