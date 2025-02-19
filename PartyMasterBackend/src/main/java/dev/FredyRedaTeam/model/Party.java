package dev.FredyRedaTeam.model;

import java.util.HashMap;
import java.util.LinkedList;
import org.json.*;

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

    public int receiveAction(String uuid, String target, JSONObject content) {
        Action action = new Action(uuid, target, content);
        switch (action.target[0]) {
            case "player" :
                switch (action.target[1]) {
                    case "quit" :
                        quit(action);
                        break;
                    case "join" :
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

    public String assignUuid(String preferedUuid) {
        return preferedUuid;
    }

    public int join(Action action) {
        Player player = players.get(action.uuid);
        if (this.players.size() < MAX_PLAYER_BY_PARTY) {
            this.players.put(player.getUuid(), player);
            this.eventQueues.put(player.getUuid(), new LinkedList<>());
            return 0;
        } else {
            return 1;
        }
    }

    public void quit(Action action) {
        Player player = players.get(action.uuid);
        this.players.remove(player.getUuid());
        this.eventQueues.remove(player.getUuid());

    }

    public String toJson() {
        return "{}";
    }
}
