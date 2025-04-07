package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

public class DeathEvent implements Event {



    @Override
    public String getType() {
        return "Games.loup.StateEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        return obj;
    }
}
