package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class StateEvent implements Event {

    private final List<UUID> table;
    private final HashSet<Card> cards;

    public HashSet<Card> getCards() {
        return cards;
    }

    public List<UUID> getTable() {
        return table;
    }

    public StateEvent(List<UUID> table, HashSet<Card> cards) {
        this.table = table;
        this.cards = cards;
    }

    @Override
    public String getType() { return "Games.loup.StateEvent"; }

    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("type", getType());
        JSONArray table = new JSONArray();
        this.table.forEach(uuid -> table.put(uuid.toString()));
        obj.put("table", table);
        JSONArray cards = new JSONArray();
        this.cards.forEach(card -> cards.put(card.id));
        obj.put("cards", cards);
        return obj;
    }
}
