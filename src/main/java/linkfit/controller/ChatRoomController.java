package linkfit.controller;

import java.util.List;
import linkfit.dto.ChatRoomDTO;
import linkfit.dto.UserName;
import linkfit.service.ChatRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDTO>> room() {
        List<ChatRoomDTO> list = chatRoomService.findAllRoom();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<ChatRoomDTO> createRoom(@RequestBody UserName userName) {
        ChatRoomDTO chatRoom = chatRoomService.createChatRoom(userName.user1(), userName.user2());
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoom);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomDTO> roomInfo(@PathVariable("roomId") String roomId) {
        ChatRoomDTO chatRoom = chatRoomService.findRoomById(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(chatRoom);
    }
}
