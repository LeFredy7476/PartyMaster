package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

class TurnEvent implements Event {

    private final UUID currentPlayer;
    private final int direction;

    public TurnEvent(UUID currentPlayer, int direction) {
        this.currentPlayer = currentPlayer;
        this.direction = direction;
    }

    public UUID getCurrentPlayer() {
        return currentPlayer;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public String getType() {
        return "Games.uno.TurnEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        obj.put("currentPlayer", this.currentPlayer.toString());
        obj.put("direction", this.direction);
        return obj;
    }
}
