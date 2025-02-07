package dev.FredyRedaTeam.model.Games;

import dev.FredyRedaTeam.model.Game;

public class Uno implements Game {
    private int id;

    public static final String name = "Uno";

    public int getId() {
        return this.id;
    }

    public static void main(String[] args) {
        Game a = new Uno();
        System.out.println(a.name);
    }
}
