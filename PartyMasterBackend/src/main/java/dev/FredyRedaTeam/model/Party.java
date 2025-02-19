package dev.FredyRedaTeam.model;

import java.util.HashMap;
import java.util.LinkedList;

public class Party {
    public static final int MAX_PLAYER_BY_PARTY = 20;
    public static final int MAX_CHAT_HISTORY = 50;
    private final HashMap<String, Player> players = new HashMap<>();
    private final HashMap<String, LinkedList<Event>> eventQueues = new HashMap<>();
    private String party_master;
    private String room;
    private Game game;
    private final LinkedList<Message> chat = new LinkedList<>();

    public void queueEventForAllPlayer(Event event) {
        for (LinkedList<Event> eventQueue : eventQueues.values()) {
            eventQueue.add(event);
        }
    }

    public int join_party(Player player) {
        if (this.players.size() < MAX_PLAYER_BY_PARTY) {
            this.players.put(player.getUuid(), player);
            this.eventQueues.put(player.getUuid(), new LinkedList<>());
            return 0;
        } else {
            return 1;
        }
    }

    public void quit_party(Player player) {
        this.players.remove(player.getUuid());
        this.eventQueues.remove(player.getUuid());

    }

    public String toJson() {
        return "{}";
    }
}
