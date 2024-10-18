package linkfit.service;

import linkfit.dto.ChatRoomDTO;
import linkfit.entity.ChatRoomEntity;
import linkfit.exception.NotFoundException;
import linkfit.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    // 모든 채팅방을 반환 (Entity -> DTO 변환)
    public List<ChatRoomDTO> findAllRoom() {
        List<ChatRoomEntity> chatRooms = chatRoomRepository.findAll();
        Collections.reverse(chatRooms);  // 최신 순으로 정렬
        return chatRooms.stream()
            .map(ChatRoomEntity::toDto)  // Entity -> DTO 변환
            .collect(Collectors.toList());
    }

    // 채팅방 ID로 찾기 (Entity -> DTO 변환)
    public ChatRoomDTO findRoomById(String id) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not found."));
        return chatRoom.toDto();  // Entity -> DTO 변환
    }

    // 채팅방 생성 (DTO -> Entity 변환 후 저장)
    public ChatRoomDTO createChatRoom(String user1, String user2) {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO(null, user1, user2);
        ChatRoomEntity chatRoomEntity = chatRoomDTO.toEntity();  // DTO -> Entity 변환
        chatRoomRepository.save(chatRoomEntity);  // DB에 저장
        return chatRoomEntity.toDto();  // 저장된 후 Entity -> DTO 변환
    }
}
