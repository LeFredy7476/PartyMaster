package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import org.json.JSONObject;

public class QuestionSpe {
    private int id;
    private String question;
    private String reponse1;
    private int niveauQuestion;

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

    public int getNiveauQuestion() {
        return niveauQuestion;
    }

    public void setNiveauQuestion(int niveauQuestion) {
        this.niveauQuestion = niveauQuestion;
    }


    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        out.put("question", question);
        out.put("reponse1", reponse1);
        out.put("niveauQuestion", niveauQuestion);
        return out;
    }
}
