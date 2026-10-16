package Game;// Imports //
import Game.ExceptionWrapper.HandleException;

import java.util.ArrayList;
import java.util.Stack;

import java.util.Scanner; // java built in utilities
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

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

    // Generates new file with pack, writing to sets of 4 same cards ranging from 1 to 2n.
    static void generatePack(int numPlayers, String fileName) {
        try {
            FileWriter myWriter = new FileWriter(fileName + ".txt");
            for (int i = 0; i < 2 * numPlayers; i++) {
                // TODO: Test first, then make random.
                for (int j = 0; j < 4; j++) {myWriter.write(i);}
            }
            myWriter.close();  // must close manually
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Writes to pack file in random order, ensuring values are shuffled before being distributed.
    }

    // Helper reads pack file, distributing Cards to Players and CardDecks.
    static void distributePack(String packFileName, int playerCount) {
        int counter = 0; // Counter keeps track of every 4 cards ending up in new hand/deck.

        Player[] players = new Player[playerCount];
        CardDeck[] cardDecks = new CardDeck[playerCount];

        // TODO: implement temporary ArrayList and stack to populate constructors.

        try (Scanner myReader = new Scanner(new File(packFileName + ".txt"))) {
            while (myReader.hasNextLine()) {
                if (counter % 4 == 0) {
                    //players[i] = new Game.Player(i);
                    //cardDecks[i] = new Game.CardDeck(i);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        System.out.println("Welcome to Game.CardGame\n");
        System.out.println("Please input the number of players:");

        Scanner Input = new Scanner(System.in);
        String NumberOfPlayers = Input.nextLine();
        int plrCount = 0;

        // Converts number of players to int, handles type and value exceptions.
        try {
            plrCount = Integer.parseInt(NumberOfPlayers);

        } catch (Exception ex) {
            // most likely user didn't enter a number
            HandleException.InvalidInputExceptionNumberFormat(ex);
        }

        if (plrCount < 2) {
            // throw exception. at least two players are required to start the game
            Exception InvalidPlayerCountException = new IllegalArgumentException();
            HandleException.InvalidPlayerCountException(InvalidPlayerCountException, plrCount);
        }

        System.out.println("Please input the card pack filename:");
        String packName = "";
        try {
            packName = Input.nextLine();
        } catch (Exception e){
            // TODO: handle invalid file name
        }

        generatePack(plrCount, packName);

        // Calls helper function to spawn threads, keeps list for debugging/testing.
        distributePack(packName, plrCount);
        //Thread[] PlayerThreads = createPlayerThreads(plrCount, players);


        // TODO: add end condition triggered by thread, writes to deck files and outputs
    }

}





