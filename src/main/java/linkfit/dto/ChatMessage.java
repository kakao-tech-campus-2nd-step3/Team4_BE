package linkfit.dto;

public record ChatMessage(MessageType type, String roomId, String sender, String message) {

    public enum MessageType {
        ENTER, TALK, EXIT, MATCH, MATCH_REQUEST;
    }
}
