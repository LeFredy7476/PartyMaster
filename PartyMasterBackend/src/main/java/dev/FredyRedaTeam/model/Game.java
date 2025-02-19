package dev.FredyRedaTeam.model;

public interface Game {

    int receiveAction(Action action);
    void tick();
    void init(Party party);
}
