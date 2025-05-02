package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;
import dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion.Question;

import java.util.UUID;

public class QuestionEvent implements Event {

    private final int id;
    private  final String question;
    private  final String reponse1;
    private  final String reponse2;
    private  final String reponse3;
    private  final String reponse4;
    private  final String typeQuestion;



    public QuestionEvent(int id,String question,String reponse1,String reponse2,String reponse3,String reponse4,String typeQuestion) {

        this.id=id;
        this.question=question;
        this.reponse1=reponse1;
        this.reponse2=reponse2;
        this.reponse3=reponse3;
        this.reponse4=reponse4;
        this.typeQuestion=typeQuestion;

    }
    public int getId(){return id;}
    public String getQuestion(){return question;}
    public String getReponse1(){return reponse1;}
    public String getReponse2(){return reponse2;}
    public String getReponse3(){return reponse3;}
    public String getReponse4(){return reponse4;}
    public String getTypeQuestion(){return typeQuestion ;}




    // --- JSON utility ---

    @Override
    public String getType() {
        return "Games.JeuxQuestion.QuestionEvent";
    }

    @Override
    public JSONObject toJson(UUID uuid) {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("question", question);
        obj.put("reponse1",reponse1);
        obj.put("reponse2",reponse2);
        obj.put("reponse3",reponse3);
        obj.put("reponse4",reponse4);
        obj.put("typeQuestion",typeQuestion);
        return obj;
    }
}
