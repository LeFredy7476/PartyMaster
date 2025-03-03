package dev.FredyRedaTeam.PartyMasterBackend.model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

public class LobbyHome implements Game {

    private Lobby lobby;

    private int selectedGame;
    private final HashMap<UUID, Integer> playerSuggestion = new HashMap<>();

    public Lobby getLobby() {
        return lobby;
    }

    @Override
    public Response receiveAction(Action action) {
        switch (action.getTarget()[1]) {
            case "select":
                return select(action);
            case "apply":
                return apply(action);
        }
        Response r = new Response(1, new JSONObject());
        r.getData().put("r", "InvalidAction");
        return r;
    }

    public Response select(Action action) {
        int targetGame = action.getContent().getInt("game");
        if (lobby.getLobbyMaster().equals(action.getUuid())) {
            return new Response();
        } else {
            return new Response();
        }
    }

    public Response apply(Action action) {
        if (lobby.getLobbyMaster().equals(action.getUuid())) {
            return new Response();
        } else {
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "NoPermission");
            return r; // REFUSED
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void init(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
