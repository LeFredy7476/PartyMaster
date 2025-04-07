package dev.FredyRedaTeam.PartyMasterBackend.model;

import org.json.JSONObject;

public class JoinEvent implements Event {

    private final Player player;
    private final long timestamp;

    public JoinEvent(Player player) {
        this.player = player;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String getType() {
        return "JoinEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        out.put("type", getType());
        out.put("player", this.player.toJson());
        out.put("timestamp", this.timestamp);
        return out;
    }
}
