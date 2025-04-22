package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

class DrawEvent implements Event {

    private final UUID target;
    private final JSONArray cards;

    public DrawEvent(UUID target, JSONArray cards) {
        this.target = target;
        this.cards = cards;
    }
    public UUID getTarget() {
        return target;
    }
    public JSONArray getCards() {
        return cards;
    }


    @Override
    public String getType() { return "Games.uno.DrawEvent"; }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        obj.put("target", this.target.toString());
        obj.put("cards", this.cards);
        return obj;
    }
}
