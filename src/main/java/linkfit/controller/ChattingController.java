package linkfit.controller;

import java.util.List;
import linkfit.annotation.Login;
import linkfit.controller.Swagger.ChattingControllerDocs;
import linkfit.dto.ChatResponse;
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
public class ChattingController implements ChattingControllerDocs {

    ChattingService chattingService;

    public ChattingController(ChattingService chattingService) {
        this.chattingService = chattingService;
    }

    @GetMapping("/room")
    public ResponseEntity<List<ChattingRoomResponse>> getMyChatRooms(@Login Token token) {
        List<ChattingRoomResponse> responses = chattingService.findJoinedRooms(token.id(),
            token.role());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{pathId}")
    public ResponseEntity<ChatResponse> getAllMessages(@Login Token token,
        @PathVariable("pathId") Long pathId) {
        ChatResponse response = chattingService.findRoomAndMessage(token.id(), token.role(),
            pathId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
