package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import org.json.JSONObject;

import java.util.UUID;

public class Joueur {
    private final UUID uuid;
    private Role role;
    private boolean vivant;

    private UUID amour;

    public Joueur(UUID uuid, Role role) {
        this.uuid = uuid;
        this.role = role;
        this.vivant = true;
        this.amour = null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isVivant() {
        return vivant;
    }

    public void setVivant(boolean vivant) {
        this.vivant = vivant;
    }

    public UUID getAmour() {
        return amour;
    }

    public void setAmour(UUID amour) {
        this.amour = amour;
    }



    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("role", this.role.toString());
        obj.put("vivant", this.vivant);
        if (this.amour == null) {
            obj.put("amour", JSONObject.NULL);
        } else {
            obj.put("amour", this.amour.toString());
        }
        return obj;
    }
}
