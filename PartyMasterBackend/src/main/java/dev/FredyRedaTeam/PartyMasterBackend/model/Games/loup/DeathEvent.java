package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class DeathEvent implements Event {
    private final UUID defunt;
    private final Joueur joueur;

    public DeathEvent(UUID defunt, Joueur joueur){
        this.defunt = defunt;
        this.joueur = joueur;
    }
    public UUID getDefunt(){return defunt;}

    public Joueur getJoueur() {
        return joueur;
    }

    @Override
    public String getType() {
        return "Loup.DeathEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("defunt",this.defunt);
        obj.put("joueur",this.joueur.toJson());
        obj.put("type", getType());
        return obj;
    }
}
