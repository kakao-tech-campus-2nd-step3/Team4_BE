package linkfit.component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SessionManager {

    // 방 별로 사용자와 역할을 함께 관리하기 위한 Map (roomId -> (userId -> (role -> WebSocketSession)))
    private Map<Long, Map<Long, Map<String, WebSocketSession>>> sessionMap = new ConcurrentHashMap<>();

    // 세션 추가
    public void addSession(Long roomId, Long userId, String role, WebSocketSession session) {
        sessionMap.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>())
            .computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
            .put(role, session);
    }

    // 세션 가져오기 (userId와 role로 구분)
    public WebSocketSession getSession(Long roomId, Long userId, String role) {
        return sessionMap.getOrDefault(roomId, new ConcurrentHashMap<>())
            .getOrDefault(userId, new ConcurrentHashMap<>())
            .get(role);
    }

    // 세션 제거 (userId와 role로 구분)
    public void removeSession(Long roomId, Long userId, String role) {
        Map<Long, Map<String, WebSocketSession>> userSessions = sessionMap.get(roomId);
        if (userSessions != null) {
            Map<String, WebSocketSession> roleSessions = userSessions.get(userId);
            if (roleSessions != null) {
                roleSessions.remove(role);
                if (roleSessions.isEmpty()) {
                    userSessions.remove(userId);
                }
            }
            if (userSessions.isEmpty()) {
                sessionMap.remove(roomId);
            }
        }
    }

    // 방에 있는 모든 세션 가져오기
    public Collection<WebSocketSession> getSessions(Long roomId) {
        Map<Long, Map<String, WebSocketSession>> userSessions = sessionMap.get(roomId);
        if (userSessions == null) {
            return Collections.emptyList();
        }
        return userSessions.values().stream()
            .flatMap(roleMap -> roleMap.values().stream())
            .collect(Collectors.toList());
    }
}
