package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

class StateEvent implements Event {

    private final JSONObject state;

    public StateEvent(JSONObject state) {
        this.state = state;
    }

    public JSONObject getJSONObject() {
        return state;
    }

    @Override
    public String getType() { return "Uno.StateEvent"; }

    @Override
    public JSONObject toJson() {
        JSONObject obj = this.state;
        obj.put("type", getType());
        return obj;
    }
}
