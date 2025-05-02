package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class QuestionResultatEvent implements Event {

    private final int id;
    private final String bonneReponse;

    public QuestionResultatEvent(int id,String bonneReponse){
        this.id=id;
        this.bonneReponse=bonneReponse;
    }
    public int getId(){return  id;}
    public String getBonneReponse(){return bonneReponse;}

    @Override
    public String getType() {return "Games.JeuxQuestion.QuestionResultatEvent";}

    @Override
    public JSONObject toJson(UUID uuid) {
        JSONObject object=new JSONObject();
        object.put("id",id);
        object.put("bonneReponse",bonneReponse);
        return object;
    }
}
