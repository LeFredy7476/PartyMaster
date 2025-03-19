package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class StateEvent implements Event {

    private final long timestamp;
    private final GameState gameState;

    public StateEvent(GameState gameState) {
        this.timestamp = System.currentTimeMillis();
        this.gameState = gameState;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public GameState getGameState() {
        return gameState;
    }

    // --- JSON utility ---

    @Override
    public String getType() {
        return "Games.loup.StateEvent";
    }

    @Override
    public JSONObject toJson(UUID uuid) {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        return obj;
    }
}
