package linkfit.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import linkfit.entity.User;

@Primary
@Repository("userRepository")
public interface UserRepository extends PersonRepository<User> {

	
}
