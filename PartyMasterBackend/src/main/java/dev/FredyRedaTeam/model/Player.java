package dev.FredyRedaTeam.model;

public class Player {
    private String uuid;
    private String name;
    private String icon;

    public Player(String uuid, String name, String icon) {
        this.uuid = uuid;
        this.name = name;
        this.icon = icon;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
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
