package linkfit.controller;

import linkfit.dto.ChatMessage;
import linkfit.repository.ChatRoomRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    //private final ChatRoomRepository  chatRoomRepository;

    public ChatController(SimpMessageSendingOperations messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("chat/message")
    public void message(ChatMessage message) {

        messagingTemplate.convertAndSend("/sub/chat/room/" + message.roomId(), message);
    }
}
