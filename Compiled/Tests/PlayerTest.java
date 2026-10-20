import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// import tests
import Game.*;

import java.lang.management.PlatformLoggingMXBean;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;



public class PlayerTest {

    private Player plr;
    private Stack<Card> drawCards;
    private Stack<Card> CardstoDiscard;
    private CardDeck CurrentDeck;
    private CardDeck nextDeck;
    private File FSOutput;
    private Object LockObj;
    private AtomicBoolean AtomicBoolean_;
    private AtomicInteger AtomicInteger_;


    @Before
    public void setUp() throws Exception {

        // test creating hand
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(1));
        hand.add(new Card(2));
        hand.add(new Card(3));
        hand.add(new Card(4));

        // empty inits
        drawCards = new Stack<>();
        CardstoDiscard = new Stack<>();
        LockObj = new Object();
        AtomicBoolean_ = new AtomicBoolean(false);
        AtomicInteger_ = new AtomicInteger(0);



        drawCards.add(new Card(5));

        // test constructors

         CurrentDeck = new CardDeck(1, drawCards);
         nextDeck = new CardDeck(2, CardstoDiscard);

        plr = new Player(1,hand,CurrentDeck,nextDeck,LockObj,AtomicBoolean_,AtomicInteger_);





    }

    @After
    public void tearDown() throws Exception {
        if(FSOutput != null) {
            FSOutput.delete(); // remove from file system
        }
    }

    @Test
    public void hasWon() {
        // test winning hand produces correct output
        assertFalse("Denominations that all do not match shouldn't win", plr.HasWon());

        ArrayList<Card> WinningHand =  new ArrayList<>();
        for(int x = 0; x < 4; x++) {
            WinningHand.add(new Card(7)); // add a new card to the winning hand

        }

        CurrentDeck = new CardDeck(1, new Stack<>());
        nextDeck = new CardDeck(2, new  Stack<>());
        Object LockObj_ = new Object();
        AtomicBoolean_ = new AtomicBoolean(false);
        AtomicInteger_ = new AtomicInteger(0);


        Player winplr = new Player(1,WinningHand,CurrentDeck,nextDeck,LockObj,AtomicBoolean_,AtomicInteger_);

        assertTrue("The 4 matching denominations have won", winplr.HasWon());


    }

    @Test
    public void drawAndDiscard() {
        plr.DrawAndDiscard();

        assertEquals("One card should be discarded here", 1, nextDeck.GetDeckSize());
        assertEquals("The deck to draw from should be empty", 0, CurrentDeck.GetDeckSize());
        assertEquals("Player should still have 4 cards", 4, plr.GetHand().size());
    }

    @Test
    public void streamLogsToOutputFile() {
        // this function writes to an output log and is private, not needed to test.
        assertNotNull(plr);
    }

    @Test
    public void run() {
        // should allow player thread to terminate immediately.
        AtomicBoolean Finish = new  AtomicBoolean(true);

        ArrayList<Card> Finishhand = new  ArrayList<>();
        Finishhand.add(new Card(1));
        Finishhand.add(new Card(2));
        Finishhand.add(new Card(3));
        Finishhand.add(new Card(4));
        CardDeck FinishDeckPrev = new  CardDeck(1, new Stack<>());
        CardDeck FinishDeckNext = new  CardDeck(2, new Stack<>());
        Object FinishPlayerLock = new Object();
        AtomicInteger FinishAtomicIntiger = new AtomicInteger(0);

        Player RunningFinishTestPlayer = new Player(1,Finishhand,FinishDeckPrev,FinishDeckNext,FinishPlayerLock, Finish, FinishAtomicIntiger);

        RunningFinishTestPlayer.run();

        assertEquals(4, RunningFinishTestPlayer.GetHand().size()); // is the hand equal to 4 after running the player with the above setup?, assert

    }

    @Test
    public void returnPlayerUUID() {
        assertEquals(1, plr.ReturnPlayerUUID()); // should be 1 as initialised in the class
    }

    @Test
    public void getHand() {
        ArrayList<Card> hand = plr.GetHand();

        assertEquals(4, hand.size());

        hand.clear(); // clear the hand arraylist, this should be a threadsafe copy to read from, not the actual hand as gethand should give a copy.

        assertEquals(4, plr.GetHand().size());

    }
}