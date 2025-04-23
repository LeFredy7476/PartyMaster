package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import org.json.JSONObject;

import java.util.UUID;

public class Joueur {
    private UUID uuid;
    private int point=0;


    public Joueur(UUID uuid, int point ){
        this.uuid=uuid;
        this.point=point;
    }
    public JSONObject toJson(){
        JSONObject object=new JSONObject();
        object.put("point",this.point);
        return object;

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

public void addPoint(int ajouter){
        this.point+=ajouter;
}

public void removePoint(int ajouter){
        this.point-=ajouter;
    }
}
