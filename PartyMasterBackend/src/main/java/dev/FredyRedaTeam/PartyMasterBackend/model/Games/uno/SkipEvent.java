package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

class SkipEvent implements Event {

    private final UUID target;

    public SkipEvent(UUID target) {
        this.target = target;
    }

    public UUID getTarget() {
        return target;
    }

    @Override
    public String getType() {
        return "Uno.SkipEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        obj.put("target", this.target.toString());
        return obj;
    }
}
