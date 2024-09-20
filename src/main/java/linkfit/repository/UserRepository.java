package linkfit.repository;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import linkfit.entity.User;

@Primary
@Repository
public interface UserRepository extends PersonRepository<User> {

	Optional<User> findByEmail(String email);
}
