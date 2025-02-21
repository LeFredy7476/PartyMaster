package dev.FredyRedaTeam.model;

import java.util.UUID;

public class Player {
    private UUID uuid;
    private String name;
    private String icon;

    public Player(UUID uuid, String name, String icon) {
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
