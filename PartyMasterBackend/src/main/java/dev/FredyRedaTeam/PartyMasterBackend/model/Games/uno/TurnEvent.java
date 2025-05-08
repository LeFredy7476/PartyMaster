package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

class TurnEvent implements Event {

    private final UUID currentPlayer;
    private final int direction;
    private final Color currentColor;
    private final int currentCard;

    public TurnEvent(UUID currentPlayer, int direction, Color currentColor, int currentCard) {
        this.currentPlayer = currentPlayer;
        this.direction = direction;
        this.currentColor = currentColor;
        this.currentCard = currentCard;
    }

    public UUID getCurrentPlayer() {
        return currentPlayer;
    }

    public int getDirection() {
        return direction;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public int getCurrentCard() {
        return currentCard;
    }

    @Override
    public String getType() {
        return "Uno.TurnEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        obj.put("currentPlayer", this.currentPlayer.toString());
        obj.put("direction", this.direction);
        obj.put("currentColor", this.currentColor);
        obj.put("currentCard", this.currentCard);
        return obj;
    }
}
