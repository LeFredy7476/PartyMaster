package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class DeathEvent implements Event {
    private final UUID defunt;
    private final long timestamp;

    public DeathEvent(UUID defunt,long timestamp){
        this.defunt=defunt;
        this.timestamp=System.currentTimeMillis();
    }
    public UUID getDefunt(){return defunt;}

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String getType() {
        return "Loup.DeathEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("defunt",this.defunt);
        obj.put("timestamp",this.timestamp);
        obj.put("type", getType());
        return obj;
    }
}
