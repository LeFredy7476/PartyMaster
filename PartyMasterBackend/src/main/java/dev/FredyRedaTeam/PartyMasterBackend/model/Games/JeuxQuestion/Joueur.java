package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import java.util.UUID;

public class Joueur {
    private UUID uuid;
    private int point;

    public Joueur(UUID uuid, int point ){
        this.uuid=uuid;
        this.point=point;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }


}
