package dev.FredyRedaTeam.PartyMasterBackend.model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class LobbyHome implements Game {

    public static class HighlightEvent implements Event {

        private final int game;
        private final long timestamp;

        public long getTimestamp() {
            return timestamp;
        }

        public int getGame() {
            return game;
        }

        public HighlightEvent(int game) {
            this.timestamp = System.currentTimeMillis();
            this.game = game;
        }

        @Override
        public String getType() {
            return "LobbyHome.HighlightEvent";
        }

        @Override
        public JSONObject toJson() {
            JSONObject obj = new JSONObject();
            obj.put("type", getType());
            obj.put("timestamp", this.timestamp);
            obj.put("game", this.game);
            return obj;
        }
    }

    public static class SuggestEvent implements Event {

        private final int game;
        private final UUID uuid;
        private final long timestamp;

        public long getTimestamp() {
            return timestamp;
        }

        public UUID getUuid() {
            return uuid;
        }

        public int getGame() {
            return game;
        }

        public SuggestEvent(int game, UUID uuid) {
            this.timestamp = System.currentTimeMillis();
            this.game = game;
            this.uuid = uuid;
        }

        @Override
        public String getType() {
            return "LobbyHome.SuggestEvent";
        }

        @Override
        public JSONObject toJson() {
            JSONObject obj = new JSONObject();
            obj.put("type", getType());
            obj.put("timestamp", this.timestamp);
            obj.put("game", this.game);
            return obj;
        }
    }

    private Lobby lobby;

    private Integer selectedGame;
    private final HashMap<UUID, Integer> suggestion = new HashMap<>();

    public Lobby getLobby() {
        return lobby;
    }

    public LobbyHome() {}

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
        int targetGame = action.getData().getInt("game");
        if (lobby.getLobbyMaster().equals(action.getUuid())) {
            this.selectedGame = targetGame;
            this.lobby.queueEventForAllPlayer(new HighlightEvent(targetGame));
            return new Response();
        } else {
            this.suggestion.put(action.getUuid(), targetGame);
            this.lobby.queueEventForAllPlayer(new SuggestEvent(targetGame, action.getUuid()));
            return new Response();
        }
    }

    public Response apply(Action action) {
        if (lobby.getLobbyMaster().equals(action.getUuid())) {
            if (selectedGame != null) {

                this.lobby.startGame(selectedGame);

                return new Response();
            } else {
                Response r = new Response(3, new JSONObject());
                r.getData().put("r", "NoGameSelected");
                return r; // REFUSED
            }
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
    public String getType() {
        return "LobbyHome";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        obj.put("selected_game", Objects.requireNonNullElse(selectedGame, JSONObject.NULL));
        obj.put("suggestion", this.suggestion);
        return obj;
    }
}
