package linkfit.controller;

import java.util.List;
import linkfit.annotation.Login;
import linkfit.dto.ChattingRoomResponse;
import linkfit.dto.MessageResponse;
import linkfit.dto.Token;
import linkfit.service.ChattingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
public class ChattingRoomController {

    ChattingService chattingService;

    public ChattingRoomController(ChattingService chattingService) {
        this.chattingService = chattingService;
    }

    @GetMapping
    public ResponseEntity<List<ChattingRoomResponse>> getMyChatRooms(@Login Token token){
        List<ChattingRoomResponse> responses =  chattingService.findJoinedRooms(token.id(),token.role());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<MessageResponse>> getAllMessages(@PathVariable Long roomId){
        List<MessageResponse> responses = chattingService.findAllMessages(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }


}
