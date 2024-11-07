package linkfit.controller;

import java.util.List;
import linkfit.annotation.Login;
import linkfit.controller.Swagger.ChattingControllerDocs;
import linkfit.dto.ChatResponse;
import linkfit.dto.ChattingRoomResponse;
import linkfit.dto.MessageResponse;
import linkfit.dto.Token;
import linkfit.service.ChattingService;
import linkfit.status.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
public class ChattingController implements ChattingControllerDocs {

    ChattingService chattingService;

    public ChattingController(ChattingService chattingService) {
        this.chattingService = chattingService;
    }

    @GetMapping
    public ResponseEntity<List<ChattingRoomResponse>> getMyChatRooms(@Login Token token) {
        List<ChattingRoomResponse> responses = chattingService.findJoinedRooms(token.id(),
            token.role());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/searchRoom/{opponentId}")
    public ResponseEntity<ChatResponse> startChatting(@Login Token token,
        @PathVariable("opponentId") Long opponentId) {
        if(token.role() == Role.USER) {
            ChatResponse response = chattingService.findChatRoom(token.id(), opponentId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        ChatResponse response = chattingService.findChatRoom(opponentId, token.id());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<MessageResponse>> getAllMessages(@PathVariable Long roomId){
        List<MessageResponse> responses = chattingService.findAllMessages(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}