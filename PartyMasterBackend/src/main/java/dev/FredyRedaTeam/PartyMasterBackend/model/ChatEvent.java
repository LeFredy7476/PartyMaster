package dev.FredyRedaTeam.PartyMasterBackend.model;

import org.json.JSONObject;

import java.util.UUID;

public class ChatEvent implements Event {

    private final long timestamp;
    private final Message message;

    public ChatEvent(Message message) {
        this.timestamp = System.currentTimeMillis();
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
    public String getType() {
        return "ChatEvent";
    }

    @Override
    public JSONObject toJson(UUID uuid) {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        return obj;
    }
}
