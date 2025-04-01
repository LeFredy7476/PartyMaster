package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class TraitreEvent implements Event {

    private final long timestamp;
    private final UUID uuid;
    private final Role role;

    public TraitreEvent(UUID uuid, Role role) {
        this.timestamp = System.currentTimeMillis();
        this.uuid = uuid;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Role getRole() {
        return role;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // --- JSON utility ---

    @Override
    public String getType() {
        return "Games.loup.TraitreEvent";
    }

    @Override
    public JSONObject toJson(UUID uuid) {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        return obj;
    }
}
