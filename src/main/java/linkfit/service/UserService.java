package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import linkfit.dto.UserBodyInfoRequest;
import linkfit.dto.UserRequest;
import linkfit.dto.UserResponse;
import linkfit.entity.User;
import linkfit.entity.UserBodyInfo;
import linkfit.exception.InvalidIdException;
import linkfit.repository.UserBodyInfoRepository;
import linkfit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserBodyInfoRepository userBodyInfoRepository;

    @Autowired
    public UserService(
        UserRepository userRepository,
        UserBodyInfoRepository userBodyInfoRepository
    ) {
        this.userRepository = userRepository;
        this.userBodyInfoRepository = userBodyInfoRepository;
    }

    public UserResponse getProfile(String authorization) {
        // jwt토큰 파싱하여 id정보 추출 구현
        Long userId = 0L;
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        return new UserResponse(user);
    }

    public void updateProfile(String authorization, UserRequest userRequest) {
        // jwt토큰 파싱하여 id정보 추출하는 로직 구현
        Long userId = 0L;
        User origin = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        User newUser = new User(origin, userRequest);
        userRepository.save(newUser);
    }

    public void registerBodyInfo(String authorization, UserBodyInfoRequest userBodyInfoRequest) {
        // jwt토큰 파싱하여 id정보 추출하는 로직 구현
        Long userId = 0L;
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        UserBodyInfo userBodyInfo = new UserBodyInfo(user, userBodyInfoRequest);
        userBodyInfoRepository.save(userBodyInfo);
    }

    public Page<UserBodyInfo> getAllBodyInfo(String authorization, Pageable pageable) {
        // jwt토큰 파싱하여 id정보 추출하는 로직 구현
        Long userId = 0L;
        return userBodyInfoRepository.findAllByUserId(userId, pageable);
    }
}
