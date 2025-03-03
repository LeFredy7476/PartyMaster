package dev.FredyRedaTeam.PartyMasterBackend.model;

import java.util.*;

import org.json.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lobby {
    public static final int MAX_PLAYER_BY_LOBBY = 20;
    public static final int MAX_CHAT_HISTORY = 50;
    private static final HashMap<String, Lobby> lobbies = new HashMap<>();
    private static Random random = new Random();

    public static boolean isInstance(String room) {
        return lobbies.containsKey(room);
    }

    public static Lobby getInstance(String room) {
        return lobbies.get(room);
    }

    public static Set<String> getAllRooms() {
        return lobbies.keySet();
    }

    public static void init() {
        // nothing to do here
    }

    private static String generateRoom() {
        String characters = "0123456789abcdefghijklmnopqrstuvwxyz";
        String out = "";
        do {
            for (int i = 0; i < 8; i++) {
                out = out + characters.charAt(random.nextInt(36));
            }
        } while (!isInstance(out));
        return out;
    }

    public static void main(String[] args) {
        init();
    }




    private final HashMap<UUID, Player> players = new HashMap<>();
    private final HashMap<UUID, LinkedList<Event>> eventQueues = new HashMap<>();
    private UUID lobbyMaster;
    private final String room;
    private Game game;
    private final LinkedList<Message> chat = new LinkedList<>();
    private long lastTick = System.currentTimeMillis();


    public Lobby() {
        this.room = generateRoom();
        lobbies.put(room, this);
        this.game = new LobbyHome();
        this.game.init(this);
    }

    /**
     * UNSAFE! Use only for debug purpose
     * @param room : the code the Lobby
     */
    public Lobby(String room) {
        this.room = room;
        lobbies.put(room, this);
        this.game = new LobbyHome();
        this.game.init(this);
    }

    public HashMap<UUID, Player> getPlayers() {
        return players;
    }

    public HashMap<UUID, LinkedList<Event>> getEventQueues() {
        return eventQueues;
    }

    public UUID getLobbyMaster() {
        return lobbyMaster;
    }

    public String getRoom() {
        return room;
    }

    public Game getGame() {
        return game;
    }

    public LinkedList<Message> getChat() {
        return chat;
    }

    public long getLastTick() {
        return lastTick;
    }

    public String getAssignNameRegex() {
        return assignNameRegex;
    }

    public Pattern getAssignNamePattern() {
        return assignNamePattern;
    }

    public void queueEventForAllPlayer(Event event) {
        for (LinkedList<Event> eventQueue : eventQueues.values()) {
            eventQueue.add(event);
        }
    }

    public void tick() {
        this.lastTick = System.currentTimeMillis();
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
    public Response receiveAction(UUID uuid, String target, JSONObject content) {
        Action action = new Action(uuid, target, content);
        switch (action.getTarget()[0]) {
            case "player":
                switch (action.getTarget()[1]) {
                    case "quit":
                        return quit(action);
                    case "join":
                        return join(action);
                    case "kick":
                        return kick(action);
                }
                break;
            case "chat" :
                switch (action.getTarget()[1]) {
                    case "send":
                        return sendMessage(action);
                }
                break;
            case "game" :
                return game.receiveAction(action);
            case "tick" :
                return null; // TODO: handle the event queue flushing
        }
        Response r = new Response(1, new JSONObject());
        r.getData().put("r", "InvalidAction");
        return r;
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

    public Response join(Action action) {
        if (this.players.size() < MAX_PLAYER_BY_LOBBY) {
            try {
                UUID uuid = this.assignUuid(action.getUuid());
                String name = action.getContent().getString("name");
                name = this.assignName(name);
                int icon = action.getContent().getInt("icon");
                Player player = new Player(uuid, name, icon);
                this.players.put(player.getUuid(), player);
                this.eventQueues.put(player.getUuid(), new LinkedList<>());
                return new Response(0); // OK
            } catch (JSONException e) {
                Response r = new Response(1, new JSONObject());
                r.getData().put("r", "InvalidRequest");
                return r; // ERROR
            }
        } else {
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "LobbyFull");
            return r; // REFUSED
        }
    }

    public Response kick(Action action) {
        if (action.getUuid().equals(this.lobbyMaster)) {
            try {
                UUID kickTarget = UUID.fromString(action.getContent().getString("kickTarget"));
                if (this.players.containsKey(kickTarget)) {
                    this.players.remove(kickTarget);
                    this.eventQueues.remove(kickTarget);
                    this.queueEventForAllPlayer(new TerminationEvent(kickTarget));
                    return new Response(); // OK
                } else {
                    Response r = new Response(2, new JSONObject());
                    r.getData().put("r", "NotInLobby");
                    return r; // IGNORED
                }
            } catch (JSONException e) {
                Response r = new Response(1, new JSONObject());
                r.getData().put("r", "InvalidRequest");
                return r; // ERROR
            }
        } else {
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "NoPermission");
            return r; // REFUSED
        }
    }

    public Response quit(Action action) {
        if (this.players.containsKey(action.getUuid())) {

            // remove player
            this.players.remove(action.getUuid());
            this.eventQueues.remove(action.getUuid());

            boolean isLobbyMaster = action.getUuid().equals(this.lobbyMaster);

            if (isLobbyMaster) {
                // disconnect all players if the lobby master logs out
                // force players to receive TerminationEvent upon next tick
                this.eventQueues.clear();
                this.players.clear();
            } else {
                // signal player departure
                this.queueEventForAllPlayer(new TerminationEvent(action.getUuid()));
            }
            return new Response(0); // OK
        } else {
            return new Response(2); // IGNORED
        }
    }

    public Response sendMessage(Action action) {
        String content = action.getContent().getString("content");
        // put a censor here if needed
        Message message = new Message("user all", action.getUuid(), content);
        // for private message (i.e. werewolves), filter the players here
        queueEventForAllPlayer(new ChatEvent(message.getTimestamp(), message));
        if (this.chat.size() >= MAX_CHAT_HISTORY) {
            this.chat.removeFirst();
        }
        this.chat.addLast(message);
        return new Response(); // OK
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
        out.put("lobby_master", this.lobbyMaster);
        out.put("players", this.playersToJson());
        out.put("game", this.game.toJson());
        out.put("chat", this.chatToJson());
        return out;
    }
}
