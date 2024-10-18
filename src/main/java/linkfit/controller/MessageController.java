package linkfit.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import linkfit.dto.MessageRequest;
import linkfit.entity.ChattingRoom;
import linkfit.entity.Message;
import linkfit.service.ChattingRoomService;
import linkfit.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final ChattingRoomService chattingRoomService;
    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;  // SimpMessageSendingOperations 주입

    public MessageController(ChattingRoomService chattingRoomService,
        MessageService messageService, SimpMessageSendingOperations messagingTemplate) {
        this.chattingRoomService = chattingRoomService;
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;  // 생성자에 SimpMessageSendingOperations 추가
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Valid MessageRequest request) throws Exception {
        // 요청에서 roomId 및 메시지 데이터 처리
        ChattingRoom chattingRoom = chattingRoomService.findRoomById(request.roomId());
        Message message = new Message(chattingRoom, request.content(), request.sender(), LocalDateTime.now());


        // 메시지 DB에 저장
        messageService.addMessage(message);

        // 동적으로 /topic/room/{roomId} 경로로 메시지 전송
        System.out.println(request.roomId());
        messagingTemplate.convertAndSend("/sub/topic/room/" + request.roomId(), message);
    }
}
