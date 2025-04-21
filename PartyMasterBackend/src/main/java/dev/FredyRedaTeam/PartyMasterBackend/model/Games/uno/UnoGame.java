package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import dev.FredyRedaTeam.PartyMasterBackend.model.Action;
import dev.FredyRedaTeam.PartyMasterBackend.model.Game;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class UnoGame implements Game {

    private Lobby lobby;

    private LinkedList<Card> deck = new LinkedList<>();
    private LinkedList<Card> flush = new LinkedList<>();

    private final HashMap<UUID, UnoPlayer> players = new HashMap<>();
    private UnoPlayer getPlayer(UUID uuid) { return players.get(uuid); }
    private final LinkedList<UUID> table = new LinkedList<>();
    private int turn = 0;
    private int round = 0;
    private UUID currentPlayer;


    /**
     * game:
     *
     * @param action
     * @return
     */
    @Override
    public Response receiveAction(Action action) {
        return null;
    }

    @Override
    public void tick() {

    }

    @Override
    public void init(Lobby lobby) {
        this.lobby = lobby;
        this.lobby.getPlayers().keySet().forEach(uuid -> {
            players.put(uuid, new UnoPlayer(uuid));
        });

        int id = 0;
        Color[] colors = { Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW };
        for (Color color : colors) {
            for (int i = 0; i < 13; i++) {
                this.deck.add(new Card(id++, i, color));
                this.deck.add(new Card(id++, i, color));
            }
        }
        for (int i = 0; i < 4; i++) {
            this.deck.add(new Card(id++, 13, Color.MULTI));
        }
        for (int i = 0; i < 4; i++) {
            this.deck.add(new Card(id++, 14, Color.MULTI));
        }
        Collections.shuffle(this.deck);



    }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public JSONObject toJson() {
        return null;
    }
}
