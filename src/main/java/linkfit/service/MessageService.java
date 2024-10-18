package linkfit.service;

import linkfit.entity.Message;
import linkfit.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void addMessage(Message message) {
        messageRepository.save(message);
    }
}
