package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.UUID;

public class QuestionSpeEvent implements Event {
    private int id;
    private String question;
    private int niveauQuestion;

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
        return "Games.JeuxQuestion.QuestionSpeEvent";
    }

    @Override
    public JSONObject toJson(UUID uuid) {
        return null;
    }
}
