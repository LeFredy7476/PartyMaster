package dev.FredyRedaTeam.PartyMasterBackend.model;
import org.json.*;

import java.util.UUID;

public class Action {
    private final UUID uuid;
    private final long timestamp;
    private final String[] target;
    private final JSONObject content;

    public Action(UUID uuid, String target, JSONObject content) {
        this.uuid = uuid;
        this.timestamp = System.currentTimeMillis();
        this.target = target.split(":", 5);
        this.content = content;
        System.out.println(this.getTarget()[0]);
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

    public JSONObject getContent() {
        return content;
    }
}
