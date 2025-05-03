package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

class PlayEvent implements Event {

    private final int card;
    private final UUID player;

    public PlayEvent(int card, UUID player) {
        this.card = card;
        this.player = player;
    }

    public int getCard() {
        return card;
    }

    public UUID getPlayer() {
        return player;
    }

    @Override
    public String getType() { return "Games.uno.PlayEvent"; }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        obj.put("player", this.player.toString());
        obj.put("card", this.card);
        return obj;
    }
}
