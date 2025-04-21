package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import dev.FredyRedaTeam.PartyMasterBackend.model.Action;
import dev.FredyRedaTeam.PartyMasterBackend.model.Game;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class UnoGame implements Game {

    // state

    private Lobby lobby;

    private final LinkedList<Card> deck = new LinkedList<>();
    private final LinkedList<Card> flush = new LinkedList<>();

    private final HashMap<UUID, UnoPlayer> players = new HashMap<>();
    private UnoPlayer getPlayer(UUID uuid) { return players.get(uuid); }
    private final LinkedList<UUID> table = new LinkedList<>();
    private int turn = 0;
    private int direction = 1;
    private UUID currentPlayer;
    private Color currentColor;
    private Card currentCard;

    // utility function

    public void reshuffle() {
        for (int i = 0; i < flush.size() - 1; i++) {
            deck.addLast(flush.removeLast());
        }
        Collections.shuffle(deck);
        lobby.queueEventForAllPlayer(new StateEvent(toJson()));
    }

    private void draw(UnoPlayer player, int amount) {
        if (amount > 0) {
            JSONArray drawn = new JSONArray();
            for (int i = 0; i < amount; i++) {
                Card card = this.deck.pollFirst();
                if (card == null) {
                    reshuffle();
                    card = this.deck.removeFirst();
                }
                player.addCard(card);
                drawn.put(card.id);
            }
            this.lobby.queueEventForAllPlayer(new DrawEvent(player.uuid, drawn));
        }
    }

    private void applyRotation() {
        int nbPlayers = table.size();
        this.turn = (turn + direction + nbPlayers) % nbPlayers;
        this.currentPlayer = table.get(turn);
    }

    private void play(UnoPlayer player, Card card) {
        player.removeCard(card);
        this.flush.addFirst(card);
        this.currentCard = card;
        this.currentColor = card.getColor();
        this.lobby.queueEventForAllPlayer(new PlayEvent(card.id, player.uuid));
        if (card.shouldFlip()) {
            this.direction = -direction;
        }
        applyRotation();
        draw(getPlayer(currentPlayer), card.shouldDraw());
        if (card.shouldSkip()) {
            this.lobby.queueEventForAllPlayer(new SkipEvent(currentPlayer));
            applyRotation();
        }

    }

    private void play(UnoPlayer player, Card card, Color color) {
        play(player, card);
        this.currentColor = color;
    }

    // actions

    public Response actionPlayNormal(Action action) {
        /* {
            "card": <int>
        } */
        assert action.getUuid().equals(currentPlayer);
        Card card = Card.get(action.getData().getInt("card"));
        assert 
    }

    // interface implementation

    /**
     * game:draw
     * game:play:normal
     * game:play:color
     * game:state
     *
     * @param action
     * @return
     */
    @Override
    public Response receiveAction(Action action) {
        try {
            return switch (action.getTarget(1)) {
                case "draw" -> null;
                case "play" -> switch (action.getTarget(2)) {
                    case "normal" -> null;
                    case "color" -> null;
                    case null, default -> null;
                };
                case "state" -> {
                    JSONObject state = toJson();
                    yield new Response(0, state);
                }
                case null, default -> null;
            };
        } catch (AssertionError e) {
            JSONObject out = new JSONObject();
            out.put("r", "ActionError");
            return new Response(3);
        }
    }

    @Override
    public void tick() {} // time limit are not yet implemented and will probably not be implemented at all

    @Override
    public void init(Lobby lobby) {
        this.lobby = lobby;
        this.lobby.getPlayers().keySet().forEach(uuid -> {
            players.put(uuid, new UnoPlayer(uuid));
            table.add(uuid);
        });

        Collections.shuffle(table);

        int id = 0;
        Color[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW };
        for (Color color : colors) {
            for (int i = 0; i < 13; i++) {
                deck.add(new Card(id++, i, color));
                deck.add(new Card(id++, i, color));
            }
        }
        for (int i = 0; i < 4; i++) {
            deck.add(new Card(id++, 13, Color.MULTI));
        }
        for (int i = 0; i < 4; i++) {
            deck.add(new Card(id++, 14, Color.MULTI));
        }

        Collections.shuffle(deck);
        for (int i = 0; i < 7; i++) {
            for (UUID uuid : table) {
                getPlayer(uuid).addCard(deck.pollFirst());
            }
        }
        do {
            Card card = deck.removeFirst();
            flush.addFirst(card);
            this.currentColor = card.getColor();
            this.currentCard = card;
        } while (flush.getFirst().getColor() == Color.MULTI);
        this.currentPlayer = table.get(turn);

        this.lobby.queueEventForAllPlayer(new StateEvent(toJson()));
    }

    @Override
    public String getType() { return "Uno"; }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        JSONArray table = new JSONArray();
        this.table.forEach(uuid -> table.put(uuid.toString()));
        obj.put("table", table);
        JSONObject players = new JSONObject();
        this.players.forEach((uuid, player) -> {
            players.put(uuid.toString(), player.toJson());
        });
        obj.put("players", players);
        JSONArray deck = new JSONArray();
        this.deck.forEach(card -> deck.put(card.id));
        obj.put("deck", deck);
        JSONArray flush = new JSONArray();
        this.flush.forEach(card -> flush.put(card.id));
        obj.put("flush", flush);
        obj.put("currentPlayer", this.currentPlayer);
        obj.put("currentColor", this.currentColor.toString());
        obj.put("currentCard", this.currentCard.id);
        return obj;
    }
}
