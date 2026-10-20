import Game.Card;
import Game.CardGame;
import Game.Player;
import Game.CardDeck;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
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

    @Test
    public void testDistributePack() {

    }

}
