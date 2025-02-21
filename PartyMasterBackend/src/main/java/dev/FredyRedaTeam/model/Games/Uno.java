package dev.FredyRedaTeam.model.Games;

import dev.FredyRedaTeam.model.Action;
import dev.FredyRedaTeam.model.Game;
import dev.FredyRedaTeam.model.Party;

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
    public int receiveAction(Action action) {
        return 0;
    }

    @Override
    public void tick() {

    }
}
