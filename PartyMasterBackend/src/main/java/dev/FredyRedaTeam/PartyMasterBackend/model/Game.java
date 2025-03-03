package dev.FredyRedaTeam.PartyMasterBackend.model;

import org.json.JSONObject;

public interface Game {

    Response receiveAction(Action action);
    void tick();
    void init(Lobby lobby);

    // --- JSON utility ---

    JSONObject toJson();
}
