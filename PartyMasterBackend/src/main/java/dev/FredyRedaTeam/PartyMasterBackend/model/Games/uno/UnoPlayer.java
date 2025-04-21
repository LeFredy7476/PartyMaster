package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.UUID;

class UnoPlayer {

    public final UUID uuid;

    private final HashSet<Card> cards = new HashSet<>();

    public UnoPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    public boolean hasWon() {
        return cards.isEmpty();
    }

    public HashSet<Card> getCards() { return cards; }

    public JSONArray toJson() {
        JSONArray arr = new JSONArray();
        this.cards.forEach(card -> arr.put(card.id));
        return arr;
    }
}
