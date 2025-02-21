package dev.FredyRedaTeam.model;

import org.json.JSONObject;

import java.util.UUID;

public interface Event {
    JSONObject toJson(UUID uuid);
}
