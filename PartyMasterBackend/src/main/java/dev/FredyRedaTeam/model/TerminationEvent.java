package dev.FredyRedaTeam.model;

import java.util.UUID;
import org.json.*;

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

    public JSONObject toJson(UUID uuid) {
        JSONObject out = new JSONObject();
        out.append("type", getType());
        out.append("uuid", uuid);
        out.append("target", this.target);
        out.append("timestamp", this.timestamp);
        return out;
    }
}
