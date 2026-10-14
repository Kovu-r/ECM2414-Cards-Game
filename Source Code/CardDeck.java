// Card deck class
// Handles the state of the card deck and exposes functions which allow atomic actions of
// draw and replace to take place.
// TODO: Decide whether to use queue or stack for cards
import java.util.Queue;
import java.util.PriorityQueue;

public class CardDeck {
    private final int deckNumber;
    private Card[] cards;
    public CardDeck(int deckNumberIn, Card[] cardsIn) {
        deckNumber = deckNumberIn;
        cards = cardsIn;
    }

    public Card[] getCards() {
        return cards;
    }
}
