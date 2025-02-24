package dev.FredyRedaTeam.model;

import org.json.JSONObject;

import java.util.UUID;

public interface Game {

    Return receiveAction(Action action);
    void tick();
    void init(Party party);

    // --- JSON utility ---

    JSONObject toJson();
}
