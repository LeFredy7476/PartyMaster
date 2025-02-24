package dev.FredyRedaTeam.model;

import org.json.JSONObject;

import java.util.UUID;

public class Player {
    private UUID uuid;
    private String name;
    private int icon;

    public Player(UUID uuid, String name, int icon) {
        this.uuid = uuid;
        this.name = name;
        this.icon = icon;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    // --- JSON utility ---

    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        out.put("uuid", this.uuid);
        out.put("name", this.name);
        out.put("icon", this.icon);
        return out;
    }
}
