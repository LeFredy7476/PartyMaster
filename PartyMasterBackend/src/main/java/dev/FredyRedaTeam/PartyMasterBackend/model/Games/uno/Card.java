package dev.FredyRedaTeam.PartyMasterBackend.model.Games.uno;

import org.json.JSONObject;

import java.util.HashMap;

class Card {

    private static final HashMap<Integer, Card> cards = new HashMap<>();

    public static Card get(int id) {
        return cards.get(id);
    }

    // Every card must be unique for the system to manage cards.
    // Shared with the frontend to facilitate communication.
    // There is 2 instance of each card (2 "green#3" are present in the game)
    public final int id;

    // 0..9, pass=10, flip=11, p2=12, color=13, p4=14
    private final int number;
    private final Color color;

    public Card(int id, int number, Color color) {
        this.id = id;
        this.number = number;
        this.color = color;
        cards.put(id, this);
    }

    public int getNumber() {
        return number;
    }

    public Color getColor() {
        return color;
    }

    public boolean shouldFlip() {
        return this.number == 11;
    }

    public int shouldDraw() {
        if (this.number == 12) {
            return 2;
        } else if (this.number == 14) {
            return 4;
        } else {
            return 0;
        }
    }

    public boolean shouldSkip() {
        return this.number == 10
            || this.number == 12
            || this.number == 14;
    }

    public boolean shouldChooseColor() {
        return this.color == Color.MULTI;
    }

    public boolean canBePlayed(Card card, Color currentColor) {
        if (this.color == Color.MULTI) {
            return true;
        } else {
            if (card.color == Color.MULTI) {
                return this.color == currentColor;
            } else {
                return this.number == card.number
                    || this.color == card.color;
            }
        }
    }
}
