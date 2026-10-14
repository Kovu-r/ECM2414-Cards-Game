// this class handles the behaviour of each player
// handles creating and managing each player's thread.
// imp runnable for thread creation

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import ExceptionWrapper.*;

public class Player implements Runnable {

    private final int InitialisationPlayerUUID;
    private Card[] hand;
    // TODO: make hand and methods thread-safe

    // basic constructor.
    public Player(int initialisationPlayerUUID, Card[] cards) {
        this.InitialisationPlayerUUID = initialisationPlayerUUID;
        this.hand = cards;
    }

    // TODO: Implement these, have access to array of decks, cards from deck, decide on queue or stack.
    // TODO: Make design choices on how to handle draw/discard on CardDeck's end.
    public void discard() {
        // Checks for first that is not preferred denomination to remove.
        for (int i = 0; i < hand.length; i++) {
            if (hand[i].GetCardDenomination() != InitialisationPlayerUUID) {

            }
        }
    }

    public void draw() {

    }

    @Override
    public void run() { // using runnable interface
        System.out.println("Player " + this.InitialisationPlayerUUID + " Started with Thread " + Thread.currentThread().threadId()); // getid() is depricated

    }
}
