package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;



public class StateEvent implements Event {

    private final long timestamp;
    private final GameStateJ gameStateJ;

    public StateEvent(GameStateJ gameStateJ) {
        this.timestamp = System.currentTimeMillis();
        this.gameStateJ=gameStateJ;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public GameStateJ getGameState() {
        return gameStateJ;
    }

    // --- JSON utility ---

    @Override
    public String getType() {
        return "JeuxQuestion.StateEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        obj.put("state",gameStateJ.toString());
        return obj;
    }
}
