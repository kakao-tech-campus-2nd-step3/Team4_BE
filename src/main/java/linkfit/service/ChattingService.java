package linkfit.service;

import java.time.LocalDateTime;
import java.util.List;

import linkfit.dto.ChatResponse;
import linkfit.dto.ChattingRoomResponse;
import linkfit.dto.MessageRequest;
import linkfit.dto.MessageResponse;
import linkfit.dto.Token;
import linkfit.entity.ChattingRoom;
import linkfit.entity.Message;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.repository.ChattingRoomRepository;
import linkfit.repository.MessageRepository;
import linkfit.repository.TrainerRepository;
import linkfit.repository.UserRepository;
import linkfit.status.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChattingService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final MessageRepository messageRepository;

    public ChattingService(ChattingRoomRepository chattingRoomRepository,
        UserRepository userRepository, TrainerRepository trainerRepository,
        MessageRepository messageRepository) {
        this.chattingRoomRepository = chattingRoomRepository;
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public ChatResponse findChatRoom(Long userId, Long trainerId) {
        User user = getUser(userId);
        Trainer trainer = getTrainer(trainerId);
        ChattingRoom room = getChattingRoom(user, trainer);
        return new ChatResponse(room.getId());
    }

    private ChattingRoom getChattingRoom(User user, Trainer trainer) {
        return chattingRoomRepository.findByUserAndTrainer(user, trainer)
            .orElseGet(() -> chattingRoomRepository.save(new ChattingRoom(user, trainer)));
    }

    //Token의 ID값으로 자신이 속해있는 채팅방 찾기
    public List<ChattingRoomResponse> findJoinedRooms(Long id, Role role) {
        if (role.equals(Role.USER)) {
            return findUserJoinedRooms(id);
        }
        return findTrainerJoinedRooms(id);
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
        User user = getUser(userId);
        return chattingRoomRepository.findAllByUser(user).stream()
            .map(chattingRoom -> {
                Message lastMessage = messageRepository
                    .findFirstByChattingRoomOrderBySendTimeDesc(chattingRoom)
                    .orElse(null);
                return chattingRoom.toUserDto(lastMessage);
            })
            .toList();
    }

    private List<ChattingRoomResponse> findTrainerJoinedRooms(Long trainerId) {
        Trainer trainer = getTrainer(trainerId);
        return chattingRoomRepository.findAllByTrainer(trainer).stream()
            .map(chattingRoom -> {
                Message lastMessage = messageRepository
                    .findFirstByChattingRoomOrderBySendTimeDesc(chattingRoom)
                    .orElse(null);
                return chattingRoom.toTrainerDto(lastMessage);
            })
            .toList();
    }

    private ChattingRoom findChattingRoom(Long chattingRoomId) {
        return chattingRoomRepository.findById(chattingRoomId)
            .orElseThrow(() -> new NotFoundException("not.found.chattingroom"));
    }

}
