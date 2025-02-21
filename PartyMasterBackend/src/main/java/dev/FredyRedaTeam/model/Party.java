package dev.FredyRedaTeam.model;

import java.util.HashMap;
import java.util.LinkedList;
import org.json.*;
import java.util.UUID;

public class Party {
    public static final int MAX_PLAYER_BY_PARTY = 20;
    public static final int MAX_CHAT_HISTORY = 50;
    private final HashMap<UUID, Player> players = new HashMap<>();
    private final HashMap<UUID, LinkedList<Event>> eventQueues = new HashMap<>();
    private UUID partyMaster;
    private String room;
    private Game game;
    private final LinkedList<Message> chat = new LinkedList<>();
    private long lastTick = System.currentTimeMillis();

    public void queueEventForAllPlayer(Event event) {
        for (LinkedList<Event> eventQueue : eventQueues.values()) {
            eventQueue.add(event);
        }
    }


    /**
     * result code :
     * - 0 : OK
     * - 1 : ERROR
     * - 2 : IGNORED
     * - 3 : REFUSED
     * - 4 : BANNED
     *
     * @param uuid client's uuid
     * @param target the path to redirect the data to
     * @param content the data itself
     * @return result code
     */

    public int receiveAction(UUID uuid, String target, JSONObject content) {
        Action action = new Action(uuid, target, content);
        switch (action.getTarget()[0]) {
            case "player":
                switch (action.getTarget()[1]) {
                    case "quit":
                        return quit(action);
                    case "join":
                        join(action);
                        break;
                }
                break;
            case "chat" :
                System.out.println("chat");
                break;
        }
        return 0;
    }

    public LinkedList<Event> fetchEvents(UUID uuid) {
        if (this.players.containsKey(uuid)) {
            return this.eventQueues.replace(uuid, new LinkedList<>());
        } else {
            LinkedList<Event> out = new LinkedList<>();
            out.add(new TerminationEvent(uuid));
            return out;
        }
    }

    public UUID assignUuid(UUID preferedUuid) {
        UUID uuid = preferedUuid;
        while (this.players.containsKey(uuid) || uuid == null) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }

    public int join(Action action) {
        Player player = players.get(action.getUuid());
        if (this.players.size() < MAX_PLAYER_BY_PARTY) {
            this.players.put(player.getUuid(), player);
            this.eventQueues.put(player.getUuid(), new LinkedList<>());
            return 0; // OK
        } else {
            return 3; // REFUSED
        }
    }

    public int quit(Action action) {
        if (this.players.containsKey(action.getUuid())) {

            // remove player
            this.players.remove(action.getUuid());
            this.eventQueues.remove(action.getUuid());

            boolean isPartyMaster = action.getUuid().equals(this.partyMaster);

            for (UUID uuid : this.players.keySet()) {
                if (isPartyMaster) {
                    // disconnect all players if the party master logs out
                    // force players to receive TerminationEvent upon next tick
                    this.eventQueues.clear();
                    this.players.clear();
                } else {
                    // signal player departure
                    this.eventQueues.get(uuid).add(new TerminationEvent(uuid));
                }
            }
            return 0; // OK
        } else {
            return 2; // IGNORED
        }
    }

    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        return out;
    }
}
