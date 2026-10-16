package Game;// Game.Card deck class
// Handles the state of the card deck and exposes functions which allow atomic actions of
// draw and replace to take place.

import java.util.Stack;

public class CardDeck {
    private final int deckNumber;
    private Stack<Card> cards;
    public CardDeck(int deckNumberIn, Stack<Card> cardsIn) {
        deckNumber = deckNumberIn;
        cards = cardsIn;
    }

    public Stack<Card> getCards() {
        return cards;
    }
}
