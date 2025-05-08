package dev.FredyRedaTeam.PartyMasterBackend.model;
import org.json.*;

import java.util.UUID;

public class Action {
    private final UUID uuid;
    private final long timestamp;
    private final String[] target;
    private final JSONObject data;

    public Action(UUID uuid, String target, JSONObject data) {
        this.uuid = uuid;
        this.timestamp = System.currentTimeMillis();
        String struuid = "<unknown>";
        if (uuid != null) {
            struuid = uuid.toString();
        }
        this.target = target.split(":", 5);
        this.data = data;
        System.out.println("\u001b[34mReceived Action " + target + " from player " + struuid + "\u001b[0m");
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String[] getTarget() {
        return target;
    }

    public String getTarget(int i) {
        try {
            return this.target[i];
        } catch (Exception e) {
            return null;
        }
    }

    public JSONObject getData() {
        return data;
    }
}
