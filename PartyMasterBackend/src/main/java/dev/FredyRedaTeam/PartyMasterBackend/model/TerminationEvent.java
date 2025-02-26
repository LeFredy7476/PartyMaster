package dev.FredyRedaTeam.PartyMasterBackend.model;

import java.util.UUID;
import org.json.*;

/**
 * Expected behavior from client :
 * <ul>
 * <li><strong>If client is target :</strong> redirect to the main page (log out)
 * <li><strong>If client is not target :</strong> remove target from player list
 * </ul>
 */
public class TerminationEvent implements Event {

    private final long timestamp;
    private final UUID target;

    public long getTimestamp() {
        return timestamp;
    }

    public TerminationEvent(UUID target) {
        this.timestamp = System.currentTimeMillis();
        this.target = target;
    }

    public UUID getTarget() {
        return target;
    }

    public static String getType() {
        return "TerminationEvent";
    }

    // --- JSON utility ---

    public JSONObject toJson(UUID uuid) {
        JSONObject out = new JSONObject();
        out.put("type", getType());
        out.put("uuid", uuid);
        out.put("target", this.target);
        out.put("timestamp", this.timestamp);
        return out;
    }
}
