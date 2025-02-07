package dev.FredyRedaTeam.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class Party {
    public static final int MAX_PLAYER_BY_PARTY = 20;
    private final ArrayList<Player> players = new ArrayList<>();
    private final Hashtable<Player, LinkedList<Event>> eventQueues = new Hashtable<>();

    public int join_party(Player player) {
        if (this.players.size() < MAX_PLAYER_BY_PARTY) {
            this.players.add(player);
            this.eventQueues.put(player, new LinkedList<>());
            return 0;
        } else {
            return 1;
        }
    }
}
