package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class VoteEvent implements Event {

    private final UUID uuid;
    private final UUID target;

    public VoteEvent(UUID uuid, UUID target) {
        this.uuid = uuid;
        this.target = target;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getTarget() {
        return target;
    }

    @Override
    public String getType() {
        return "Loup.VoteEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid.toString());
        json.put("target", target.toString());
        return json;
    }
}
