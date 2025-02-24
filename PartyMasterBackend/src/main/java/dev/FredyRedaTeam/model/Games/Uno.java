package dev.FredyRedaTeam.model.Games;

import dev.FredyRedaTeam.model.Action;
import dev.FredyRedaTeam.model.Game;
import dev.FredyRedaTeam.model.Party;
import dev.FredyRedaTeam.model.Return;
import org.json.JSONObject;

import java.util.UUID;

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
    public Return receiveAction(Action action) {
        return new Return();
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
