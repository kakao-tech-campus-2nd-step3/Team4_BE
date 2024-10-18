package linkfit.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import linkfit.dto.MessageRequest;
import linkfit.entity.ChattingRoom;
import linkfit.entity.Message;
import linkfit.service.ChattingService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final ChattingService chattingService;
    private final SimpMessageSendingOperations messagingTemplate;  // SimpMessageSendingOperations 주입

    public MessageController(ChattingService chattingService,
        SimpMessageSendingOperations messagingTemplate) {
        this.chattingService = chattingService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Valid MessageRequest request) throws Exception {
        Message message = chattingService.addMessage(request);
        messagingTemplate.convertAndSend("/sub/topic/room/" + request.roomId(), message.toDto());
    }
}
