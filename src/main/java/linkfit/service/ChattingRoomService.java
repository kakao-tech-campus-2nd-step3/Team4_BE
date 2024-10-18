package linkfit.service;

import java.util.List;
import java.util.stream.Collectors;
import linkfit.dto.ChattingRoomRegisterRequest;
import linkfit.entity.ChattingRoom;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.ChattingRoomRepository;
import linkfit.repository.TrainerRepository;
import linkfit.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ChattingRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;

    public ChattingRoomService(ChattingRoomRepository chattingRoomRepository,
        UserRepository userRepository, TrainerRepository trainerRepository) {
        this.chattingRoomRepository = chattingRoomRepository;
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
    }

    //ID로 채팅방 찾기
    public ChattingRoom findRoomById(Long id) {
        return chattingRoomRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("ChattingRoom not found"));
    }

    //채팅방 생성
    public void addRoom(Long userId, String role, ChattingRoomRegisterRequest request) {
        if (!role.equals("user")) {
            throw new PermissionException("Only users can add rooms");
        }
        //이미 userId, TrainerId 에 해당하는 사용자가 속해있는 대화방이 있는지 확인하는 로직 필요
        User user = getUser(userId);
        Trainer trainer = getTrainer(userId);
        chattingRoomRepository.save(request.toEntity(user, trainer));
    }

    //Token의 ID값으로 자신이 속해있는 채팅방 찾기
    //현재는 상대방의 ID만 반환, 추후에 추가정보 필요시 DTO형태로 추가하면 될것같음.
    public List<Long> findJoinedRooms(Long id, String role) {
        if (role.equals("user")) {
            return findUserJoinedRooms(id);
        }

        if (role.equals("trainer")) {
            return findTrainerJoinedRooms(id);
        }

        return null;
    }

    private Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException("not.found.trainer"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("not.found.user"));
    }

    private List<Long> findUserJoinedRooms(Long userId) {
        return chattingRoomRepository.findAllByUserId(userId).stream()
            .map(ChattingRoom::getUser)
            .map(User::getId)
            .collect(Collectors.toList());
    }

    private List<Long> findTrainerJoinedRooms(Long trainerId) {
        return chattingRoomRepository.findAllByTrainerId(trainerId).stream()
            .map(ChattingRoom::getUser)
            .map(User::getId)
            .collect(Collectors.toList());
    }
}
