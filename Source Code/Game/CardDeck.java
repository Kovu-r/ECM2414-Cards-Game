package Game;// Game.Card deck class
// Handles the state of the card deck and exposes functions which allow atomic actions of
// draw and replace to take place.

import java.util.ArrayList;
import java.util.Stack;
import Game.ExceptionWrapper.*;

public class CardDeck {
    private final int deckNumber;
    private final Stack<Card> cards;

    public CardDeck(int deckNumberIn, Stack<Card> cardsIn) {
        if (cardsIn == null) {
            NullPointerException NullPointerException = new NullPointerException();
            HandleException.CardObjectNotFoundException(NullPointerException);
        }
        this.deckNumber = deckNumberIn;
        this.cards = new Stack<>();
        this.cards.addAll(cardsIn);

    }

    public Stack<Card> getCards() {
        return cards;
    }
    public int GetDeckNumber() { return this.deckNumber; }



    public synchronized Card DrawCard() {
        if (cards.isEmpty()) {
            Exception EmptyDeckException = new NullPointerException();
            HandleException.EmptyDeckException(EmptyDeckException);
        }
        return cards.removeFirst(); // can use .remove(0) , 0 being the first index for FIFO behaviour, this is due to a stack being LIFO, to keep the
        // implementation consistent without rewriting too much code, use the stack data structure

    }

    public synchronized void DiscardCard(Card C) {
        if (C == null) {
            NullPointerException NullPointerException = new NullPointerException();
            HandleException.CardNotFoundException(NullPointerException);
        }
        cards.push(C); // pushes onto the stack, to be removed last, don't use .pop();
    }

    // for debugging

    public synchronized int GetDeckSize() {
        return cards.size();
    }

    // to prevent unwanted writes to the original card deck, impl another arraylist they can read from

    public synchronized ArrayList<Card> GetCurrentCards() {
        return new ArrayList<>(cards);
    }

}
