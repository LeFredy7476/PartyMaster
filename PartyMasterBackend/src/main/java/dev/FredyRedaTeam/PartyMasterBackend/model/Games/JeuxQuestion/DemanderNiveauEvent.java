package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class DemanderNiveauEvent implements Event {

    private int niveau;

    public DemanderNiveauEvent(int niveau){
        this.niveau=niveau;
    }
    public int getNiveau(){return niveau;}


    @Override
    public String getType() {
        return "Games.JeuxQuestion.DemanderNiveauEvent";
    }

    @Override
    public JSONObject toJson(UUID uuid) {
        return null;
    }
}
