package bookingApp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private static Map<String, Integer> sessions = new HashMap<>();

    public static String createSession(int userId) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, userId);
        return sessionId;
    }

    public static Integer getUserId(String sessionId) {
        return sessions.get(sessionId);
    }

}
