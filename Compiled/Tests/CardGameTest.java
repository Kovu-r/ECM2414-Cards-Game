import Game.Card;
import Game.CardGame;
import Game.Player;
import Game.CardDeck;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class CardGameTest {
    private CardGame game;
    private String testFileName;
    private int playerCount;

    @Before
    public void setUp() throws Exception {
        game = new CardGame();
        testFileName = "testPack";
        playerCount = 4;
        game.generatePack(playerCount, testFileName);
    }

    @Test
    public void testCreatePlayerThreads() {
        Player[] players = new Player[4];

        Thread[] expectedPlayerThread = new Thread[4];
        assertNotEquals(expectedPlayerThread, game.createPlayerThreads(4, players));
    }

    @Test
    public void testGeneratePackSize() {
        game.generatePack(4, "test_pack");
        ArrayList<Card> cards1 = new ArrayList<Card>();
        ArrayList<Card> cards2 = new ArrayList<Card>();

        CardDeck deck1 = new CardDeck(1, new Stack<>());
        CardDeck deck2 = new CardDeck(2, new Stack<>());
        CardDeck deck3 = new CardDeck(3, new Stack<>());

        Object lock = new Object();
        AtomicBoolean gameFinished = new AtomicBoolean();
        AtomicInteger winnerID = new AtomicInteger();

        Player [] testPlayers = {new Player(0, cards1, deck1, deck2, lock, gameFinished, winnerID),
                new Player(1, cards2, deck2, deck3, lock, gameFinished, winnerID)};
        Thread[] threads = game.createPlayerThreads(playerCount, testPlayers);
        Thread[] expected = {new Thread(testPlayers[0]), new Thread(testPlayers[1])};
        assertNotEquals(expected, threads);
    }

    @Test
    public void testGenerateValidPackFile() {
        Path testPath = Path.of(testFileName + ".txt");
        assertTrue(Files.exists(testPath));
    }

    @Test // test that cards are shuffled and distributed to all hands/decks correctly
    public void testDistributePack() throws IOException {
        game.distributePack(testFileName, playerCount);
        int hand_size = game.getHAND_DECK_SIZE();
        // creates an array of arraylists to compare to array of hands or decks.
        ArrayList<Integer> [] unshuffledValues = new ArrayList[playerCount];

        // create array of unshuffled values
        for (int i = 0; i < playerCount; i++) {
            unshuffledValues[i] = new ArrayList<Integer>();
            for (int j = 0; j < hand_size; j++) {
                unshuffledValues[i].add(i);
            }
        }

        CardDeck[] distributedDecks = game.getCardDecks();
        Player[] distributedPlayers = game.getPlayers();

        boolean decksShuffled = false;
        boolean handsShuffled = false;
        for (int i = 0; i < playerCount; i++) {
            assertEquals(hand_size, distributedDecks[i].getCards().size());
            assertEquals(distributedPlayers[i].GetHand().size(), hand_size);
            if (!(distributedPlayers[i].equals(unshuffledValues[i]))) {
                handsShuffled = true;
            }

            ArrayList deckArray = new ArrayList(distributedDecks[i].getCards());
            if (!(deckArray).equals(unshuffledValues[i])) {
                decksShuffled = true;
            }
        }
        assertEquals(true, decksShuffled);
        assertEquals(true, handsShuffled);
    }

    @Test
    public void testCleanUp() throws IOException {

        game.distributePack(testFileName, playerCount);

        CardDeck[] distributedDecks = game.getCardDecks();

        for(CardDeck deck : distributedDecks) {
            Path FSWritestreamOUT = Path.of("deck" + deck.GetDeckNumber() + "_output.txt");

            Files.deleteIfExists(FSWritestreamOUT); // delete if its found


        }

        game.cleanup(); // run cleanup and see if the files are generated correctly.

        for (CardDeck deck : distributedDecks) {
            Path outputfsstream = Path.of("deck" + deck.GetDeckNumber() + "_output.txt");

            String FSContents = Files.readString(outputfsstream);

            assertTrue("The cleanup program should create " + outputfsstream, Files.exists(outputfsstream)); // check if the file exists

            assertTrue("The file should contain the following header: ", FSContents.startsWith("deck" + deck.GetDeckNumber() + " contents:")); // check if the contents match the required spec output

        }


    }

}
