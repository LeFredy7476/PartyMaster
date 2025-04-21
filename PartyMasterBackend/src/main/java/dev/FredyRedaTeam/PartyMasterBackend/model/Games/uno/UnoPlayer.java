package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import java.util.HashSet;
import java.util.UUID;

public class UnoPlayer {

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
}
