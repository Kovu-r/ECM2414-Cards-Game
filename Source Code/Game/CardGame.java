package Game;// Imports //
import Game.ExceptionWrapper.HandleException;

import java.util.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

// Game class is entry point, takes number of players as input, creates and starts player threads.
public class CardGame {

    public CardGame() {}

    // Helper creates array of new started threads for each player, given input count.
    public static Thread[] createPlayerThreads(int playerCount, Player[] players) {
        Thread[] playerThreads = new Thread[playerCount];
        for(int i = 0; i < playerCount; i++) {
            playerThreads[i] = new Thread(players[i]);
            playerThreads[i].start();
        }
        return playerThreads;
    }

    // Generates new file with pack, writing to sets of 4 same cards ranging from 1 to 2n.
    public void generatePack(int numPlayers, String fileName) {
        try {
            FileWriter myWriter = new FileWriter(fileName + ".txt");
            for (int i = 0; i < 2 * numPlayers; i++) {
                for (int j = 0; j < 4; j++) {myWriter.write(i);}
            }
            myWriter.close();  // must close manually
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Integer [] loadPack(String filePath, int playerCount) {
        Integer[] cards = new Integer[8 * playerCount];

        int counter = 0;

        // Reads in cards from file.
        try (Scanner myReader = new Scanner(new File(filePath + ".txt"))) {
            cards[counter] = myReader.nextInt();
            counter++;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return cards;
    }

    // Takes integer to use collection shuffle method.
    Integer [] shuffleCards(Integer[] cards) {
        List<Integer> l = Arrays.asList(cards);
        Collections.shuffle(l);
        cards = l.toArray(new Integer[0]);
        return cards;
    }


    // Helper reads pack file, distributing Cards to Players and CardDecks.
     Player[] distributePack(String packFileName, int playerCount) {
        Player[] players = new Player[playerCount];
        CardDeck[] cardDecks = new CardDeck[playerCount];
        Integer [] cards = shuffleCards(loadPack(packFileName, playerCount));

        // Keeps track of cards in card array when adding to objects.
        int cardCounter = 0;
        for (int i = 0; i < 2 * playerCount; i++) {

            // Adds new cards to stack, creating deck objects.
            if (i % 2 == 0) {
                Stack<Card> deckCards = new Stack<Card>();
                for (int j = 0; j < 4; j++) {
                    deckCards.push(new Card(cards[cardCounter]));
                    cardCounter++;
                }
                int deckNumber = i/2;
                cardDecks[deckNumber] = new CardDeck(deckNumber, deckCards);
            }
            // Adds new cards to list, creating player objects.
            else {
                ArrayList<Card> handCards = new ArrayList<Card>();
                for (int j = 0; j < 4; j++) {
                    handCards.add(new Card(cards[cardCounter]));
                    cardCounter++;
                }
                int playerNumber = i/2;
                players[playerNumber] = new Player(playerNumber, handCards);
            }
        }
        // Used to create threads for players.
        return players;
    }

    public static void main(String[] args) {
        CardGame game = new CardGame();

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
        String packName = Input.nextLine();
        try (Scanner myReader = new Scanner(new File(packName + ".txt"))) {
        } catch (FileNotFoundException e) {
            System.out.println("Please input a valid filename:");
            e.printStackTrace();
        }

        game.generatePack(plrCount, packName);
        Player[] players = game.distributePack(packName, plrCount); // returns players to start threads
        Thread[] PlayerThreads = createPlayerThreads(plrCount, players);


        // TODO: add end condition triggered by thread, writes to deck files and outputs
    }

}
