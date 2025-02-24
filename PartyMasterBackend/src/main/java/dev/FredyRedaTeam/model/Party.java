package dev.FredyRedaTeam.model;

import java.util.HashMap;
import java.util.LinkedList;
import org.json.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
    public Return receiveAction(UUID uuid, String target, JSONObject content) {
        Action action = new Action(uuid, target, content);
        switch (action.getTarget()[0]) {
            case "player":
                switch (action.getTarget()[1]) {
                    case "quit":
                        return quit(action);
                    case "join":
                        return join(action);
                }
                break;
            case "chat" :
                switch (action.getTarget()[1]) {
                    case "send":
                        return sendMessage(action);
                }
                break;
        }
        return new Return(2, new JSONObject()); // if command is not matched
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

    public List<String> getPlayerNames() {
        return this.players.keySet().stream().map((UUID uuid) -> this.players.get(uuid).getName()).toList();
    }

    private final String assignNameRegex = "^(.*-)(\\d{2})$";
    private final Pattern assignNamePattern = Pattern.compile(assignNameRegex);
    public String assignName(String preferedName) {
        List<String> names = this.getPlayerNames();
        while (names.contains(preferedName)) {
            Matcher m = assignNamePattern.matcher(preferedName);
            if (m.matches()) {
                preferedName = m.group(1) + String.format("%02d", Integer.parseInt(m.group(2)) + 1);
            } else {
                preferedName += "-01";
            }
        }
        return preferedName;
    }

    public Return join(Action action) {
        if (this.players.size() < MAX_PLAYER_BY_PARTY) {
            try {
                UUID uuid = this.assignUuid(action.getUuid());
                String name = action.getContent().getString("name");
                name = this.assignName(name);
                int icon = action.getContent().getInt("icon");
                Player player = new Player(uuid, name, icon);
                this.players.put(player.getUuid(), player);
                this.eventQueues.put(player.getUuid(), new LinkedList<>());
                return new Return(0); // OK
            } catch (JSONException e) {
                Return r = new Return(3, new JSONObject());
                r.getData().put("r", "InvalidRequest");
                return r; // REFUSED
            }
        } else {
            Return r = new Return(3, new JSONObject());
            r.getData().put("r", "PartyFull");
            return r; // REFUSED
        }
    }

    public Return quit(Action action) {
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
            return new Return(0); // OK
        } else {
            return new Return(2); // IGNORED
        }
    }

    public Return sendMessage(Action action) {
        String content = action.getContent().getString("content");
        // put a censor here if needed
        Message message = new Message(action.getUuid(), content);
        // for private message (i.e. werewolves), filter the players here
        queueEventForAllPlayer(new ChatEvent(message.getTimestamp(), message));
        if (this.chat.size() >= MAX_CHAT_HISTORY) {
            this.chat.removeFirst();
        }
        this.chat.addLast(message);
        return new Return(); // OK
    }

    // --- JSON utility ---

    public JSONObject playersToJson() {
        JSONObject out = new JSONObject();
        this.players.forEach((UUID uuid, Player player) -> {
            out.put(uuid.toString(), player.toJson());
        });
        return out;
    }

    public JSONArray chatToJson() {
        JSONArray out = new JSONArray();
        this.chat.forEach((Message msg) -> {
            out.put(msg.toJson());
        });
        return out;
    }

    public JSONObject toJson() {
        JSONObject out = new JSONObject();
        out.put("room", this.room);
        out.put("party_master", this.partyMaster);
        out.put("players", this.playersToJson());
        out.put("game", this.game.toJson());
        out.put("chat", this.chatToJson());
        return out;
    }
}
