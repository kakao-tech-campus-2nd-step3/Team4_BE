package linkfit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import linkfit.entity.Person;

@NoRepositoryBean
public interface PersonRepository<T extends Person> extends JpaRepository<T, Long> {

    boolean existsByEmail(String email);

    Optional<T> findByEmail(String email);
}
