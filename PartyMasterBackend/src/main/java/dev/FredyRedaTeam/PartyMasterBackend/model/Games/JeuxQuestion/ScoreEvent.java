package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONObject;

import java.util.ArrayList;


public class ScoreEvent implements Event {

    private final long timestamp;
    private final ArrayList<Joueur> score;

    public ScoreEvent(ArrayList<Joueur> score) {
        this.timestamp = System.currentTimeMillis();
        this.score=score;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ArrayList<Joueur> getScore() {
        return score;
    }

    // --- JSON utility ---

    @Override
    public String getType() {
        return "JeuxQuestion.ScoreEvent";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        JSONObject scores = new JSONObject();
        for (Joueur joueur : score) {
            scores.put(joueur.getUuid().toString(), joueur.getPoint());
        }
        obj.put("scores", scores);
        return obj;
    }
}
