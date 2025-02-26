package dev.FredyRedaTeam.PartyMasterBackend.model;

import org.json.JSONObject;

import java.util.UUID;

public class Message {
    private final String type;
    private final UUID uuid;
    private final long timestamp;
    private final String content;

    public Message(String type, UUID uuid, String content) {
        this.type = type;
        this.uuid = uuid;
        this.timestamp = System.currentTimeMillis();
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    // --- JSON utility ---

    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        out.put("type", this.type);
        out.put("uuid", this.uuid);
        out.put("timestamp", this.timestamp);
        out.put("content", this.content);
        return out;
    }
}
