package Game;// this class handles the behaviour of each player
// handles creating and managing each player's thread.
// imp runnable for thread creation
import java.util.ArrayList;

public class Player implements Runnable {

    private final int InitialisationPlayerUUID;
    private ArrayList<Card> hand;
    // TODO: make hand and methods thread-safe

    // basic constructor.
    public Player(int initialisationPlayerUUID, ArrayList<Card> cards) {
        this.InitialisationPlayerUUID = initialisationPlayerUUID;
        this.hand = cards;
    }

    public void discard() {
        // Checks for first that is not preferred denomination to remove.
        for (int i = 0; i < (hand.size()); i++) {
            if (hand.get(i).GetCardDenomination() != InitialisationPlayerUUID) {

            }
        }
    }

    public void draw() {

    }

    @Override
    public void run() { // using runnable interface
        System.out.println("Game.Player " + this.InitialisationPlayerUUID + " Started with Thread " + Thread.currentThread().threadId()); // getid() is depricated

    }
}
