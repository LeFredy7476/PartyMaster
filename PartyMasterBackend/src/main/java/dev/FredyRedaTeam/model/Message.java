package dev.FredyRedaTeam.model;

import org.json.JSONObject;

import java.util.UUID;

public class Message {
    private final UUID uuid;
    private final long timestamp;
    private final String content;

    public Message(UUID uuid, String content) {
        this.uuid = uuid;
        this.timestamp = System.currentTimeMillis();
        this.content = content;
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
        out.put("uuid", this.uuid);
        out.put("timestamp", this.timestamp);
        out.put("content", this.content);
        return out;
    }
}
