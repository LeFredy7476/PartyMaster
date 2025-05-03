package dev.FredyRedaTeam.PartyMasterBackend.model;

import org.json.JSONObject;

import java.util.UUID;

public class GameChangeEvent implements Event {

    private final JSONObject game;

    public GameChangeEvent(JSONObject game) {
        this.game = game;
    }

    // --- JSON utility ---

    @Override
    public String getType() {
        return "GameChangeEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        out.put("type", getType());
        out.put("game", game);
        return out;
    }
}
