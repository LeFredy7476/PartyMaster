package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class RevelationEvent implements Event {

    private final long timestamp;
    private final UUID uuid;
    private final Role role;
    private final String sender;

    public RevelationEvent(UUID uuid, Role role, String sender) {
        this.timestamp = System.currentTimeMillis();
        this.uuid = uuid;
        this.role = role;
        this.sender = sender;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Role getRole() {
        return role;
    }

    public String getSender() {
        return sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // --- JSON utility ---

    @Override
    public String getType() {
        return "Games.loup.RevelationEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        return obj;
    }
}
