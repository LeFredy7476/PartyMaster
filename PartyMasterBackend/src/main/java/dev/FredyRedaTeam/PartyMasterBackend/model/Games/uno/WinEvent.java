package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class WinEvent implements Event {

    private final UUID player;

    public WinEvent(UUID player) {
        this.player = player;
    }

    public UUID getPlayer() {
        return player;
    }

    @Override
    public String getType() {
        return "Games.uno.WinEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        obj.put("player", this.player.toString());
        return obj;
    }
}
