package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class FinEvent implements Event {

    private final long timestamp;
    private final UUID uuid;
    private final Role role;
    ArrayList<UUID> listWinner=new ArrayList<>();

    public FinEvent(UUID uuid, Role role, ArrayList<UUID> listWinner) {
        this.timestamp = System.currentTimeMillis();
        this.uuid = uuid;
        this.role = role;
        this.listWinner = listWinner;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Role getRole() {
        return role;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // --- JSON utility ---

    @Override
    public String getType() {
        return "Games.loup.FinEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        return obj;
    }




}
