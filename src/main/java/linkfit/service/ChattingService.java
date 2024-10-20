package linkfit.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import linkfit.dto.ChattingRoomRegisterRequest;
import linkfit.dto.ChattingRoomResponse;
import linkfit.dto.MessageRequest;
import linkfit.dto.MessageResponse;
import linkfit.entity.ChattingRoom;
import linkfit.entity.Message;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.ChattingRoomRepository;
import linkfit.repository.MessageRepository;
import linkfit.repository.TrainerRepository;
import linkfit.repository.UserRepository;
import linkfit.status.Role;
import org.springframework.stereotype.Service;

@Service
public class ChattingService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    MessageRepository messageRepository;

    public ChattingService(ChattingRoomRepository chattingRoomRepository,
        UserRepository userRepository, TrainerRepository trainerRepository,
        MessageRepository messageRepository) {
        this.chattingRoomRepository = chattingRoomRepository;
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.messageRepository = messageRepository;
    }


    public void addRoom(Long userId, String role, ChattingRoomRegisterRequest request) {
        //이미 userId, TrainerId 에 해당하는 사용자가 속해있는 대화방이 있는지 확인하는 로직 필요
        User user = getUser(userId);
        Trainer trainer = getTrainer(userId);
        chattingRoomRepository.save(request.toEntity(user, trainer));
    }

    //Token의 ID값으로 자신이 속해있는 채팅방 찾기
    public List<ChattingRoomResponse> findJoinedRooms(Long id, Role role) {
        if (role == Role.USER) {
            return findUserJoinedRooms(id);
        }

        if (role == Role.TRAINER) {
            return findTrainerJoinedRooms(id);
        }

        return null;
    }

    //채팅방의 모든 메세지 가져오기
    public List<MessageResponse> findAllMessages(Long ChattingRoomId) {
        ChattingRoom chattingRoom = findChattingRoom(ChattingRoomId);
        List<Message> messages = messageRepository.findAllByChattingRoomOrderBySendTime(
            chattingRoom);
        return messages.stream().map(Message::toDto).toList();
    }

    public Message addMessage(MessageRequest request) {
        // 요청에서 roomId 및 메시지 데이터 처리
        ChattingRoom chattingRoom = findChattingRoom(request.roomId());
        Message message = new Message(chattingRoom, request.content(), request.sender(),
            LocalDateTime.now());
        messageRepository.save(message);
        return message;

    }

    private Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException("not.found.trainer"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("not.found.user"));
    }

    private List<ChattingRoomResponse> findUserJoinedRooms(Long userId) {
        return chattingRoomRepository.findAllByUserId(userId).stream()
            .map(ChattingRoom::toDto)
            .toList();
    }

    private List<ChattingRoomResponse> findTrainerJoinedRooms(Long userId) {
        return chattingRoomRepository.findAllByUserId(userId).stream()
            .map(ChattingRoom::toDto)
            .toList();
    }

    private ChattingRoom findChattingRoom(Long chattingRoomId) {
        return chattingRoomRepository.findById(chattingRoomId)
            .orElseThrow(() -> new NotFoundException("not.found.chattingroom"));
    }
}
