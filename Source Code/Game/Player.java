package Game;// this class handles the behaviour of each player
// handles creating and managing each player's thread.
// imp runnable for thread creation
import java.lang.constant.DirectMethodHandleDesc;
import java.util.ArrayList;
import Game.ExceptionWrapper.*;

import java.util.InputMismatchException;
import java.util.concurrent.atomic.*;


public class Player implements Runnable {

    private final int InitialisationPlayerUUID; // player ID
    private final ArrayList<Card> hand; // refrence doesnt change, but contents do.
    private final CardDeck PreviousDeck; // where p is plr, p-1th deck
    private final CardDeck NextDeck; //pth deck
    private final Object Lock; // Thread-safe lock, this is SHARED across players
    private final AtomicBoolean GameFinished;
    private final AtomicInteger PlayerUUIDWon;
    // TODO: make hand and methods thread-safe

    // basic constructor.
    public Player(int initialisationPlayerUUID, ArrayList<Card> InitialHand, CardDeck PreviousDeck, CardDeck NextDeck, Object Lock, AtomicBoolean GameFinished, AtomicInteger PlayerUUIDWon) {
        this.InitialisationPlayerUUID = initialisationPlayerUUID;
        this.PreviousDeck =  PreviousDeck;
        this.NextDeck = NextDeck;
        this.Lock = Lock;
        this.GameFinished = GameFinished;
        this.PlayerUUIDWon = PlayerUUIDWon;

        if (InitialHand == null || InitialHand.size() != 4) {
            // initial card hand is not correct
            Exception IncorrectSizeException = new InputMismatchException();
            HandleException.IncorrectHandSizeException(IncorrectSizeException, Thread.currentThread());

        }

        this.hand = InitialHand;

    }



   /* public void discard() {
        // Checks for first that is not preferred denomination to remove.
        for (int i = 0; i < (hand.size()); i++) {
            if (hand.get(i).GetCardDenomination() != InitialisationPlayerUUID) {

            }
        }
    }

    public void draw() {


    } Moved to CardDeck class since the action needs to be atomic here*/


    // detection of a win
    public boolean HasWon() {
        // fail conditions, this check runs at the end of each turn

        if (hand.size() < 4) { // if the hand doesnt have 4 cards.
            return false;
        }

        for (Card CurCard : hand) { // ~ foreach
           int count = 0;
           for (Card CurCard_ : hand) {
               if (CurCard.GetCardDenomination() == CurCard_.GetCardDenomination()) { // if the denominations match, then add one to the count, if the count is 4 then the player has a winning hand
                   count++;
               }
           }

           if (count >= 4) {
               //GameFinished.set(true);
               return true;

           }
        }
        return false; // else return false.
    }

    public void DrawAndDiscard() {
        synchronized (Lock) { // shared lock, cannot be defined in the main method as then it would lock the player

            if (GameFinished.get()) {
                return;
            }

            Card DrawnCard = PreviousDeck.DrawCard();
            hand.add(DrawnCard);
            //boolean _HasWon = false;

            if(HasWon()) {
                if (PlayerUUIDWon.compareAndSet(0, InitialisationPlayerUUID)) {
                    GameFinished.set(true);
                    System.out.println("Player " + this.InitialisationPlayerUUID + " has won the game!");
                }
                return;
                //System.out.println("Player " + this.InitialisationPlayerUUID + " has won the game!");
                //_HasWon = true;
            }

            // discard a card that isn't preferred
            Card CardToDisacrd = null;

            for (Card CardInHand : hand) {
                if (CardInHand.GetCardDenomination() != InitialisationPlayerUUID) {
                    CardToDisacrd = CardInHand;
                    break;
                }
            }

            if (CardToDisacrd == null) {
                // shouldn't be null as we did the win check before, therefore there should be at least one card that is not prefered, Thread safety fail, throw exec
                Exception ThreadSafetyException = new IllegalThreadStateException();
                HandleException.CardDeckIllegalRaceConditionException(ThreadSafetyException, Thread.currentThread());


            } else {
                // discard card,
                hand.remove(CardToDisacrd);

                NextDeck.DiscardCard(CardToDisacrd); // discard into the next deck
            }

            //if (_HasWon) {
            //    CardGame.Cleanup(); // run cleanup, passed concurrency checks
            //}


        }
    }





    @Override
    public void run() { // using runnable interface
        System.out.println("Game.Player " + this.InitialisationPlayerUUID + " Started with Thread " + Thread.currentThread().threadId()); // getid() is depricated
        synchronized (Lock) {
            if (GameFinished.get()) {
                return;
            }

            if (HasWon()) {
                if (PlayerUUIDWon.compareAndSet(0, InitialisationPlayerUUID)) {
                    System.out.println("Player " + this.InitialisationPlayerUUID + " has won the game (with their initial dealt hand)");
                    GameFinished.set(true);

                }
                return;
            }


            /*if (!GameFinished.get()) {
                if (!GameFinished.get() && HasWon()) {
                    System.out.println("Player " + this.InitialisationPlayerUUID + " has won the game! (with their initial hand)");
                    GameFinished.set(true);
                    return;
                }
            }*/

        }

        while (!GameFinished.get()) {
            // keep drawing, this is outside of the lock to allow players to draw independently and concurrently
            DrawAndDiscard();
        }
    }

    // for debugging

    public int ReturnPlayerUUID() {
        return this.InitialisationPlayerUUID;
    }

    public synchronized ArrayList<Card> GetHand() {
        return new ArrayList<>(this.hand);
    }



}
