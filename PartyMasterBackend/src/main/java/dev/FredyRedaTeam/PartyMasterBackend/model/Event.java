package dev.FredyRedaTeam.PartyMasterBackend.model;

import org.json.JSONObject;

import java.util.UUID;

public interface Event {

    // --- JSON utility ---

    String getType();

    JSONObject toJson();
}
