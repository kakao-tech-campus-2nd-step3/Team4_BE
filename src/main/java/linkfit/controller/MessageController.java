package linkfit.controller;

import java.time.LocalDateTime;
import linkfit.entity.ChattingRoom;
import linkfit.entity.Message;
import linkfit.service.ChattingRoomService;
import linkfit.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final ChattingRoomService chattingRoomService;
    private final MessageService messageService;

    public MessageController(ChattingRoomService chattingRoomService,
        MessageService messageService) {
        this.chattingRoomService = chattingRoomService;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(Message message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Long roomId = message.getChattingRoom().getId();
        String role = headerAccessor.getSessionAttributes().get("role").toString();

        ChattingRoom chattingRoom = chattingRoomService.findRoomById(roomId);
        messageService.addMessage(message);  // 메시지 DB에 저장
        return message;  // 이 메시지는 /topic/room/{roomId}에 구독한 클라이언트에게 전송됨
    }
}
