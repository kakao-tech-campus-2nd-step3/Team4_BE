package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import linkfit.dto.UserResponse;
import linkfit.entity.User;
import linkfit.exception.InvalidIdException;
import linkfit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getProfile(String authorization) {
        // jwt토큰 파싱하여 id정보 추출 구현
        Long userId = 0L;
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        return new UserResponse(user);
    }
}
