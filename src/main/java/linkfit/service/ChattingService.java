package linkfit.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import linkfit.dto.ChatResponse;
import linkfit.dto.ChattingRoomRegisterRequest;
import linkfit.dto.ChattingRoomResponse;
import linkfit.dto.MessageRequest;
import linkfit.dto.MessageResponse;
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
    public ChatResponse findRoomAndMessage(Long tokenId, Role role, Long pathId) {
        User user;
        Trainer trainer;
        // 역할에 따른 User, Trainer 설정
        if (role.equals(Role.USER)) {
            user = getUser(tokenId);
            trainer = getTrainer(tokenId);
        } else {
            user = getUser(pathId);
            trainer = getTrainer(tokenId);
        }

        // 채팅방 조회 또는 생성
        ChattingRoom room = findOrCreateChattingRoom(user, trainer);
        // 채팅 메시지 조회
        List<MessageResponse> messages = findAllMessages(room.getId());

        return new ChatResponse(room.getId(), messages);
    }

    private ChattingRoom findOrCreateChattingRoom(User user, Trainer trainer) {
        if (chattingRoomRepository.existsByUserAndTrainer(user, trainer)) {
            return chattingRoomRepository.findByUserAndTrainer(user, trainer);
        } else {
            ChattingRoom room = new ChattingRoom(user, trainer);
            chattingRoomRepository.save(room);
            return room;
        }
    }

    //Token의 ID값으로 자신이 속해있는 채팅방 찾기
    public List<ChattingRoomResponse> findJoinedRooms(Long id, Role role) {
        if (role.equals(Role.USER)) {
            return findUserJoinedRooms(id);
        } else {
            return findTrainerJoinedRooms(id);
        }
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
