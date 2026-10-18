import Game.Card;
import Game.CardGame;
import Game.Player;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

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
        ArrayList<Card> cards1 = new ArrayList<Card>();
        ArrayList<Card> cards2 = new ArrayList<Card>();

        Player [] testPlayers = {new Player(0, cards1), new Player(1, cards2)};
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
    public void testGeneratePackSize() {
        ArrayList<Integer> packValues = new ArrayList<Integer>();
        try (Scanner myReader = new Scanner(new File(testFileName + ".txt"))) {
            packValues.add(myReader.nextInt());
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        assertEquals(8 * playerCount, packValues.size());
    }

    @Test
    public void testGeneratePackOverwrite() {
        ArrayList<Integer> firstPackValues = new ArrayList<Integer>();
        try (Scanner myReader = new Scanner(new File(testFileName + ".txt"))) {
            firstPackValues.add(myReader.nextInt());
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Overwrites file with new larger deck in same location.
        game.generatePack(4, testFileName);
        Path testPath = Path.of(testFileName + ".txt");

        ArrayList<Integer> secondPackValues = new ArrayList<Integer>();
        try (Scanner myReader = new Scanner(new File(testFileName + ".txt"))) {
            secondPackValues.add(myReader.nextInt());
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //assertNotEquals();

    }

    @Test
    public void testDistributePackObjectQuantities() {

    }
}