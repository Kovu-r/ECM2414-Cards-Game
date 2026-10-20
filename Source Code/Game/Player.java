package Game;// this class handles the behaviour of each player
// handles creating and managing each player's thread.
// imp runnable for thread creation
import java.lang.constant.DirectMethodHandleDesc;
import java.util.ArrayList;
import Game.ExceptionWrapper.*;

import java.util.InputMismatchException;
import java.util.concurrent.atomic.*;

// imports for dealing with java FS writer
import java.util.List;
import java.io.*;

public class Player implements Runnable {

    private final int InitialisationPlayerUUID; // player ID
    private final ArrayList<Card> hand; // refrence doesnt change, but contents do.
    private final CardDeck PreviousDeck; // where p is plr, p-1th deck
    private final CardDeck NextDeck; //pth deck
    private final Object Lock; // Thread-safe lock, this is SHARED across players
    private final AtomicBoolean GameFinished;
    private final AtomicInteger PlayerUUIDWon;
    private final List<String> PlayerLogs = new ArrayList<>();
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

        //Playerlogs("[DEBUG] Player with UUID " + initialisationPlayerUUID + " has initalised with hand " + Format());

    }

    // format the hand for printing
    private String Format() {
        StringBuilder sb = new StringBuilder();

        for (Card card : hand) {
            if (sb.length() > 0) {
                sb.append(" ");
            }

            sb.append(card.GetCardDenomination());
        }

        return sb.toString();
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

    // player logs to write to output file
    private void Playerlogs(String Event) {
        PlayerLogs.add(Event);

    }


    public void DrawAndDiscard() {
        synchronized (Lock) { // shared lock, cannot be defined in the main method as then it would lock the player

            while (!GameFinished.get() && PreviousDeck.GetDeckSize() == 0) { // while a deck is exhausted, release the lock and allow another player to discard into the deck thats empty. very important.
                try {
                    Lock.wait();
                } catch (InterruptedException InterruptExc) {
                    Thread.currentThread().interrupt(); // interrupt the greedy thread and make it wait and allow another thread to execute.
                    return;
                }

            }


            if (GameFinished.get()) {
                return;
            }

            Card DrawnCard = PreviousDeck.DrawCard();
            hand.add(DrawnCard);
            Playerlogs("Player with UUID " + InitialisationPlayerUUID + " draws a " + DrawnCard.GetCardDenomination() + " from deck " + PreviousDeck.GetDeckNumber());


            //System.out.println("[DEBUG] Player" + InitialisationPlayerUUID + " drew from deck " + PreviousDeck.GetDeckNumber());
            //boolean _HasWon = false;

            //System.out.println("[DEBUG] Player" + InitialisationPlayerUUID + " hand after drawing is " + Format());

            if(HasWon()) {

                Card CardToDiscard = null;

                for  (Card Card_ : hand) {
                    if (Card_.GetCardDenomination() != InitialisationPlayerUUID) {
                        CardToDiscard = Card_;
                        break;
                    }
                }

                // if all cards are initplrUUID, discard any one

                if  (CardToDiscard == null) {
                    CardToDiscard = hand.get(0);
                }

                // remove from winning player's hand and discard to next deck
                hand.remove(CardToDiscard);
                NextDeck.DiscardCard(CardToDiscard);
                Lock.notifyAll(); // notify threads

                // debug
                //System.out.println("[DEBUG] Player " + InitialisationPlayerUUID + " discarded" + CardToDiscard.GetCardDenomination() + "into deck " + NextDeck.GetDeckNumber() + "deck has now got " + NextDeck.GetDeckSize() + " cards");


                if (PlayerUUIDWon.compareAndSet(0, InitialisationPlayerUUID)) {
                    GameFinished.set(true);
                    Lock.notifyAll(); // notify other threads
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
                //System.out.println("[DEBUG] Player " + InitialisationPlayerUUID + "is going to discard" + CardToDisacrd.GetCardDenomination());
                hand.remove(CardToDisacrd);

                NextDeck.DiscardCard(CardToDisacrd); // discard into the next deck
                Lock.notifyAll(); // notify other threads
                //System.out.println("[DEBUG] Deck" +  NextDeck.GetDeckNumber() + "Now contains " + NextDeck.GetDeckSize() + " cards");
                Playerlogs("Player with UUID " + InitialisationPlayerUUID +  " discards a " + CardToDisacrd.GetCardDenomination() + " to deck " + NextDeck.GetDeckNumber());
                Playerlogs("Player with UUID " + InitialisationPlayerUUID + " has a current hand of " + Format());
            }

            //if (_HasWon) {
            //    CardGame.Cleanup(); // run cleanup, passed concurrency checks
            //}


        }
    }

    public void StreamLogsToOutputFile(int WinnerPlayerUUID) {
        String FsName = "player" + InitialisationPlayerUUID + "_output.txt"; // format required by spec

        try (FileWriter WriteStreamObject = new FileWriter(FsName)) { // to handle I/O exception try,

            // write initial logs
            for (String Log : PlayerLogs) {
                WriteStreamObject.write(Log + System.lineSeparator()); // write with a space
            }

            if (InitialisationPlayerUUID == WinnerPlayerUUID) { // format required by spec
                WriteStreamObject.write("Player " + InitialisationPlayerUUID + " wins" + System.lineSeparator());
                WriteStreamObject.write("Player " + InitialisationPlayerUUID + " exits" +  System.lineSeparator());
                WriteStreamObject.write("Player " + InitialisationPlayerUUID + " final hand " + Format() + System.lineSeparator());

            } else {
                // current player did not win
                WriteStreamObject.write("Player " + WinnerPlayerUUID + " has informed player " + InitialisationPlayerUUID + " that player " + WinnerPlayerUUID + "has won" + System.lineSeparator());
                WriteStreamObject.write("Player " + InitialisationPlayerUUID + " exits" +  System.lineSeparator());
                WriteStreamObject.write("Player " + InitialisationPlayerUUID + " final hand " + Format() + System.lineSeparator());
            }
        } catch (IOException e) {
            HandleException.WriteStreamIOException(e);
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

                // discard a drawn card that isnt preffered to make sure the winning player only has 4 cards.
                Card CardToDisacrd = null;

                for (Card card : hand) {
                    if (card.GetCardDenomination() != InitialisationPlayerUUID) {
                        CardToDisacrd = card;
                        break;
                    }
                }
                if (CardToDisacrd == null) {
                    CardToDisacrd = hand.get(0); // if all 5 are preferred denomination, discard one card.
                }
                // remove from hand and discard to next deck
                hand.remove(CardToDisacrd);
                NextDeck.DiscardCard(CardToDisacrd);

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
            try {
                DrawAndDiscard();
            } catch (Exception e) { // if the game still isnt finished, catch the empty deck exception, happens when a thread continues to draw and never replenishes a deck after a win conditon has been satisfied.
                if (!GameFinished.get()) {
                    HandleException.EmptyDeckException(e);
                }
                break;
            }

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
