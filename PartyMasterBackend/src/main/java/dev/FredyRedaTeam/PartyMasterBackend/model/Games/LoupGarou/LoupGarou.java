package dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou;

import java.util.UUID;

public class LoupGarou {
    private UUID uuid;
    private String nom;
    private String role;


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    public boolean isVivant() {
        return vivant;
    }

    public void setVivant(boolean vivant) {
        this.vivant = vivant;
    }

    private boolean vivant;
    
}
