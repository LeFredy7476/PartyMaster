package dev.FredyRedaTeam.PartyMasterBackend.model;

import org.json.JSONObject;

public interface Event {

    // --- JSON utility ---

    String getType();

    JSONObject toJson();
}
