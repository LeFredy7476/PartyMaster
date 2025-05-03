package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class WinnerEvent implements Event {
    private final String winner;
    private final long timestamp;

    public WinnerEvent(String winner, long timestamp){
        this.winner=winner;
        this.timestamp=System.currentTimeMillis();
    }
    public String getWinner(){return winner;}

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String getType() {
        return "Games.loup.WinnerEvent";
    }

    @Override
    public JSONObject toJson(UUID uuid) {
        JSONObject obj = new JSONObject();
        obj.put("winner",this.winner);
        obj.put("timestamp",this.timestamp);
        obj.put("type", getType());
        return obj;
    }
}
