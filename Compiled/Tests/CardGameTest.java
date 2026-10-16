import Game.CardGame;
import Game.Player;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardGameTest {

    @Before
    public void setUp() throws Exception {
        CardGame game = new CardGame();
    }

    @Test
    public void testCreatePlayerThreads() {
        // TODO: Add initialised players when implemented
        Player[] players = new Player[4];
        Thread[] expectedPlayerThread = new Thread[4];
        assertEquals(expectedPlayerThread, game.createPlayerThreads(4, players));
    }

    // TODO: Finish implementing File reading for values and name/location for overwrite.
    @Test
    public void testGeneratePackSize() {
        game.generatePack(4, "test_pack");

    }

    @Test
    public void testGeneratePackFile() {
        game.generatePack(4, "test_pack");

    }

    @Test
    public void testGeneratePackRandom() {
        game.generatePack(4, "test_pack");

    }

    @Test
    public void testGeneratePackOverwrite() {
        game.generatePack(4, "test_pack");

    }

    // TODO: replicate anything external, simulate behaviour of objects/pack.
    @Test
    public void testDistributePackObjectQuantities() {

    }

    @Test
    public void testDistributePackObjectTypes() {

    }
}