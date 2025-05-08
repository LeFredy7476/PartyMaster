package dev.FredyRedaTeam.PartyMasterBackend.model;

import java.util.*;

import dev.FredyRedaTeam.PartyMasterBackend.model.Games.utils.Sql;
import dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion.QuestionGame;
import dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup.LoupGame;
import dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno.UnoGame;
import org.json.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lobby {
    public static final int MAX_PLAYER_BY_LOBBY = 20;
    public static final int MAX_CHAT_HISTORY = 50;
    public static final long ROOM_SHUTDOWN_CHRONO = 600000;
    public static final long PLAYER_SHUTDOWN_CHRONO = 150000;
    private static final HashMap<String, Lobby> lobbies = new HashMap<>();
    public static final Random random = new Random();

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
            out = "";
            for (int i = 0; i < 4; i++) {
                out = out + String.valueOf(characters.charAt(random.nextInt(36)));
            }
        } while (isInstance(out));
        return out;
    }

    public static void checkRooms() {
        long now = System.currentTimeMillis();
        // make copy of keyset in order to avoid ConcurrentModificationException
        // see: https://www.digitalocean.com/community/tutorials/java-util-concurrentmodificationexception
        Set<String> rooms = new HashSet<>(Lobby.lobbies.keySet());
        for (String room : rooms) {
            if (now - lobbies.get(room).lastTick > ROOM_SHUTDOWN_CHRONO) {
                System.out.println("\u001b[31minactivity detected for room " + room + "\u001b[0m");
                lobbies.remove(room);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        init();
    }


    private final HashMap<UUID, Player> players = new HashMap<>();
    private final HashMap<UUID, LinkedList<Event>> eventQueues = new HashMap<>();
    private UUID lobbyMaster;
    private final String room;
    private Game game;
    private final LinkedList<Message> chat = new LinkedList<>();
    private long lastTick = System.currentTimeMillis();


    public Lobby() throws Exception {
        this.room = generateRoom();
        System.out.println("\u001b[35mCreated room " + this.room + "\u001b[0m");
        lobbies.put(room, this);

        // TODO: "logger" la création du groupe dans la DB

        this.game = new LobbyHome();
        this.game.init(this);

        String idGame = this.room;
        Long tempsCreation = System.currentTimeMillis();
        Sql.InsertGame(idGame, tempsCreation);

        this.tick();
    }

    /**
     * UNSAFE! Use only for debug purpose
     * @param room : the code the Lobby
     */
    public Lobby(String room) throws Exception {
        this.room = room;
        lobbies.put(room, this);
        this.game = new LobbyHome();
        this.game.init(this);
        
        String idGame = this.room;
        Long tempsCreation = System.currentTimeMillis();
        Sql.InsertGame(idGame, tempsCreation);

        this.tick();
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
        System.out.println(event.toJson().toString());
        for (LinkedList<Event> eventQueue : eventQueues.values()) {
            eventQueue.add(event);
        }
    }

    public void queueEventForAllPlayer(Event event, UUID except) {
        for (UUID uuid : eventQueues.keySet()) {
            if (!uuid.equals(except)) {
                eventQueues.get(uuid).add(event);
            }
        }
    }

    public void queueEvent(UUID uuid, Event event) {
        System.out.println("sent to " + uuid.toString() + " : " + event.toJson().toString());
        this.eventQueues.get(uuid).add(event);
    }

    public void tick() {
        this.lastTick = System.currentTimeMillis();
        // make copy of keyset in order to avoid ConcurrentModificationException
        // see: https://www.digitalocean.com/community/tutorials/java-util-concurrentmodificationexception
        Set<UUID> uuids = new HashSet<>(this.players.keySet());
        for (UUID uuid : uuids) {
            if (System.currentTimeMillis() - this.players.get(uuid).lastTick > PLAYER_SHUTDOWN_CHRONO) {
                System.out.println("\u001b[33minactivity detected for player " + uuid.toString() + "\u001b[0m");
                if (this.lobbyMaster.equals(uuid)) {
                    this.players.clear();
                    this.eventQueues.clear();
                } else {
                    this.players.remove(uuid);
                    this.eventQueues.remove(uuid);
                    this.queueEventForAllPlayer(new TerminationEvent(uuid));
                    exitGame();
                }
            }
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
     * @param data the data itself
     * @return result code
     */
    public Response receiveAction(UUID uuid, String target, JSONObject data) {
        Action action = new Action(uuid, target, data);
        switch (action.getTarget(0)) {
            case "player":
                switch (action.getTarget(1)) {
                    case "quit":
                        return quit(action);
                    case "join":
                        return join(action);
                    case "kick":
                        return kick(action);
                }
                break;
            case "chat" :
                switch (action.getTarget(1)) {
                    case "send":
                        return sendMessage(action);
                    case "fetch":
                        Response r = new Response(0, new JSONObject());
                        r.getData().put("chat", this.chatToJson());
                        return r;
                }
                break;
            case "game" :
                return game.receiveAction(action);
        }
        Response r = new Response(2, new JSONObject());
        r.getData().put("r", "UnknownAction");
        return r;
    }

    public LinkedList<Event> fetchEvents(UUID uuid) {
        this.tick(); // refresh the room shutdown cooldown
        if (this.players.containsKey(uuid)) {
            players.get(uuid).lastTick = System.currentTimeMillis();
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

    public boolean isOpen() {
        return this.game instanceof LobbyHome && this.players.size() < MAX_PLAYER_BY_LOBBY;
    }

    public Response join(Action action) {
        if (this.game instanceof LobbyHome) {
            if (this.players.size() < MAX_PLAYER_BY_LOBBY) {
                try {
                    UUID uuid = this.assignUuid(action.getUuid());
                    String name = action.getData().getString("name");
                    name = this.assignName(name);
                    int icon = action.getData().getInt("icon");
                    Player player = new Player(uuid, name, icon);
                    this.queueEventForAllPlayer(new JoinEvent(player));
                    this.players.put(player.getUuid(), player);
                    this.eventQueues.put(player.getUuid(), new LinkedList<>());
                    if (this.lobbyMaster == null) {
                        this.lobbyMaster = uuid;
                    }
                    System.out.println("\u001b[32mplayer " + uuid.toString() + " joined room " + this.room + "\u001b[0m");
                    JSONObject out = new JSONObject();
                    out.put("uuid", uuid.toString());
                    out.put("name", name);
                    return new Response(0, out); // OK
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
        } else {
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "LobbyClosed");
            return r; // REFUSED
        }
    }

    public Response kick(Action action) {
        if (action.getUuid().equals(this.lobbyMaster)) {
            try {
                UUID kickTarget = UUID.fromString(action.getData().getString("target"));
                if (this.players.containsKey(kickTarget)) {
                    this.players.remove(kickTarget);
                    this.eventQueues.remove(kickTarget);
                    this.queueEventForAllPlayer(new TerminationEvent(kickTarget));
                    exitGame();
                    System.out.println("\u001b[36mplayer " + kickTarget.toString() + " was kicked from room " + this.room + "\u001b[0m");
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

            System.out.println("\u001b[36mplayer " + action.getUuid().toString() + " quitted room " + this.room + "\u001b[0m");

            boolean isLobbyMaster = action.getUuid().equals(this.lobbyMaster);

            if (isLobbyMaster) {
                System.out.println("\u001b[31mplayer " + action.getUuid().toString() + " was partymaster in room " + this.room + ", closing room...\u001b[0m");
                // disconnect all players if the lobby master logs out
                // force players to receive TerminationEvent upon next tick
                this.eventQueues.clear();
                this.players.clear();
            } else {
                // signal player departure
                this.queueEventForAllPlayer(new TerminationEvent(action.getUuid()));
                exitGame();
            }
            return new Response(0); // OK
        } else {
            return new Response(2); // IGNORED
        }
    }

    public void exitGame() {
        this.game = new LobbyHome();
        this.game.init(this);
        this.queueEventForAllPlayer(new GameChangeEvent(this.game.toJson()));
    }

    public static final String[] games = {"Uno", "Loup", "Question"};
    public void startGame(int i) {
        startGame(games[i]);
    }
    public void startGame(String gameName) {
        this.game = switch (gameName) {
            case "Uno" -> new UnoGame();
            case "Loup" -> new LoupGame();
            case "Question" -> new QuestionGame(); // TODO: merge le jeu de Reda
            case null, default -> new LobbyHome(); // fallback
        };
        this.game.init(this);
        this.queueEventForAllPlayer(new GameChangeEvent(this.game.toJson()));
    }


    public Response sendMessage(Action action) {
        String content = action.getData().getString("content"); // TODO: Ignore the action if the message is empty
        // put a censor here if needed
        Message message = new Message("user all", action.getUuid(), content);
        // for private message (i.e. werewolves), filter the players here
        queueEventForAllPlayer(new ChatEvent(message));
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
        if (this.lobbyMaster == null) {
            out.put("lobby_master", JSONObject.NULL);
        } else {
            out.put("lobby_master", this.lobbyMaster);
        }
        out.put("players", this.playersToJson());
        out.put("game", this.game.toJson());
        out.put("chat", this.chatToJson());
        return out;
    }
}
