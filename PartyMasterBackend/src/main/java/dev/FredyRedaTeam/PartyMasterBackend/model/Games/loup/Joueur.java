package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import java.util.UUID;

public class Joueur {

    private final UUID uuid;
    private Role role;
    private boolean vivant;
    // private boolean chef;
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

    public Joueur getAmour() {
        return amour;
    }

    public void setAmour(Joueur amour) {
        this.amour = amour;
    }
}
