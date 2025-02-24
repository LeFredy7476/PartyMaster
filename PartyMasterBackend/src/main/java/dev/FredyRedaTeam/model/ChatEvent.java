package dev.FredyRedaTeam.model;

import org.json.JSONObject;

import java.util.UUID;

public class ChatEvent implements Event {

    private final long timestamp;
    private final Message message;

    public ChatEvent(long timestamp, Message message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Message getMessage() {
        return message;
    }

    // --- JSON utility ---

    @Override
    public JSONObject toJson(UUID uuid) {
        return null;
    }
}
