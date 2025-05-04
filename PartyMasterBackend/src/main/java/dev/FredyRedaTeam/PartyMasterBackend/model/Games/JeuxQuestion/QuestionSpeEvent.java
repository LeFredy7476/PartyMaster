package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class QuestionSpeEvent implements Event {
    private final int  id;
    private final String question;
    private final  int niveauQuestion;

    public QuestionSpeEvent(int id,String question,int niveauQuestion){
        this.id=id;
        this.question=question;
        this.niveauQuestion=niveauQuestion;
    }

    public int getId(){return id;}

    public String getQuestion(){return question;}

    public int getNiveauQuestion(){return niveauQuestion;}


    @Override
    public String getType() {
        return "JeuxQuestion.QuestionSpeEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        object.put("id",this.id);
        object.put("question",this.question);
        object.put("niveauQuestion",this.niveauQuestion);
        object.put("type",getType());
        return object;
    }
}
