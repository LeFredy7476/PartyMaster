package dev.FredyRedaTeam.model.Games;

import dev.FredyRedaTeam.model.Action;
import dev.FredyRedaTeam.model.Game;
import dev.FredyRedaTeam.model.Party;
import dev.FredyRedaTeam.model.Response;
import org.json.JSONObject;

public class Uno implements Game {
    private int id;


    public static final String name = "Uno";

    public int getId() {
        return this.id;
    }


    @Override
    public void init(Party party) {

    }

    @Override
    public Response receiveAction(Action action) {
        return new Response();
    }

    @Override
    public void tick() {

    }

    // --- JSON utility ---

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }
}
