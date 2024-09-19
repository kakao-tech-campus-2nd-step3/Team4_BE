package linkfit.service;

import org.springframework.stereotype.Service;

import linkfit.entity.User;
import linkfit.repository.UserRepository;

@Service
public class UserService extends PersonService<User> {

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }
}
