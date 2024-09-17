package linkfit.service;

import org.springframework.stereotype.Service;

import linkfit.entity.User;
import linkfit.exception.ExistsEmailException;
import linkfit.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void existsByEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new ExistsEmailException("This email already exists.");
		}
	}

	public void userSave(User user) {
		userRepository.save(user);
	}
}
