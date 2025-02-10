package dev.FredyRedaTeam.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

public class Party {
    public static final int MAX_PLAYER_BY_PARTY = 20;
    private final HashMap<String, Player> players = new HashMap<>();
    private final Hashtable<String, LinkedList<Event>> eventQueues = new Hashtable<>();

    public void queueEventForAllPlayer(Event event) {
        for (Hashtable<String, LinkedList<Event>> eventQueue in eventQueues) {

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
}
