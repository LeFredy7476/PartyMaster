package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import org.json.JSONObject;

public class Question {
    private int id;
    private String question;
    private String reponse1;
    private String reponse2;
    private String reponse3;
    private String reponse4;
    private String TypeQuestion;
    private String bonneReponse;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    public String getReponse1() {
        return reponse1;
    }

    public void setReponse1(String reponse1) {
        this.reponse1 = reponse1;
    }

    public String getBonneReponse() {
        return bonneReponse;
    }

    public void setBonneReponse(String bonneReponse) {
        this.bonneReponse = bonneReponse;
    }

    public String getReponse3() {
        return reponse3;
    }

    public void setReponse3(String reponse3) {
        this.reponse3 = reponse3;
    }

    public String getReponse4() {
        return reponse4;
    }

    public void setReponse4(String reponse4) {
        this.reponse4 = reponse4;
    }

    public String getTypeQuestion() {
        return TypeQuestion;
    }

    public void setTypeQuestion(String typeQuestion) {
        TypeQuestion = typeQuestion;
    }

    public String getReponse2() {
        return reponse2;
    }

    public void setReponse2(String reponse2) {
        this.reponse2 = reponse2;
    }

public JSONObject toJson() {
        JSONObject out=new JSONObject();
        out.put("id",id);
        out.put("question",question);
        out.put("reponse1",reponse1);
        out.put("reponse2",reponse2);
        out.put("reponse3",reponse3);
        out.put("reponse4",reponse4);
        out.put("TypeQuestion",TypeQuestion);
        out.put("bonneReponse",bonneReponse);
        return out;
}


}
