// Imports //
import java.util.Scanner; // java built in utilities


// Game class is entry point, takes number of players as input, creates and starts player threads.
public class CardGame {

    // Helper creates array of new started threads for each player, given input count.
    static Thread[] createPlayerThreads(int playerCount, Player[] players) {
        Thread[] playerThreads = new Thread[playerCount];
        for(int i = 0; i < playerCount; i++) {
            playerThreads[i] = new Thread(players[i]);
            playerThreads[i].start();
        }
        return playerThreads;
    }

    // Helper reads pack file, distributing Cards to Players and CardDecks.
    static void distributePack(String packFileName) {

    }

    public static void main(String[] args) {

        System.out.println("Welcome to CardGame\n");
        System.out.println("Please input the number of players:");

        Scanner Input = new Scanner(System.in);
        String NumberOfPlayers = Input.nextLine();
        int plrCount = 0;

        // Converts number of players to int, handles type and value exceptions.
        try {
            plrCount = Integer.parseInt(NumberOfPlayers);

        } catch (Exception ex) {
            // most likely user didn't enter a number
            ExceptionWrapper.HandleException.InvalidInputExceptionNumberFormat(ex);
        }

        if (plrCount < 2) {
            // throw exception. at least two players are required to start the game
            Exception InvalidPlayerCountException = new IllegalArgumentException();
            ExceptionWrapper.HandleException.InvalidPlayerCountException(InvalidPlayerCountException, plrCount);
        }

        System.out.println("Please input the card pack filename:");
        String packName = Input.nextLine();

        int[] pack = new int[8 * plrCount]; // expects to load 8 cards for each player.

        // TODO: Add try/catch to populate pack, handle cases with invalid input or not long enough.


        // TODO: Make this into a helper function that initialises and adds sets of 4 cards.
        Player[] players = new Player[plrCount];
        CardDeck[] cardDecks = new CardDeck[plrCount];

        for (int i = 0; i < plrCount; i++) {
            //players[i] = new Player(i);
            //cardDecks[i] = new CardDeck(i);
        }

        // Calls helper function to spawn threads, keeps list for debugging/testing.
        Thread[] PlayerThreads = createPlayerThreads(plrCount, players);
    }

}





