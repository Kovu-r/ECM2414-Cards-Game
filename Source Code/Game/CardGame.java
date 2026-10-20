package Game;// Imports //
import Game.ExceptionWrapper.HandleException;

import javax.annotation.processing.FilerException;
import java.util.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// Game class is entry point, takes number of players as input, creates and starts player threads.
public class CardGame {

    private final AtomicBoolean GameFinished = new AtomicBoolean(false); // atomic boolean, multiple thread read and write safe
    private final AtomicInteger PlayerUUIDWon = new AtomicInteger(0);
    private CardDeck[] cardDecks;
    private Player[] players; // keep private refrences in this class to be able to cleanup properly

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

    // Initialise all players
    public Player[] InitialisePlayers(int PlayerCount, ArrayList<ArrayList<Card>> Hands, CardDeck[] cardDecks) {
        Player[] Players = new Player[PlayerCount];
        Object SharedLock = new Object();
        for(int i = 0; i < PlayerCount; i++) {
            Players[i] = new Player(i + 1, Hands.get(i), cardDecks[i], cardDecks[(i + 1) % PlayerCount],SharedLock, GameFinished, PlayerUUIDWon);
        }
        this.players = Players; // assign to local copy
        return Players;
    }

    // Generates new file with pack, writing to sets of 4 same cards ranging from 1 to 2n.
    public void generatePack(int numPlayers, String fileName) {
        Integer[] cardValues = new Integer[8 * numPlayers];
        // Populates with predetermined cards, shuffles and then adds from array before writing.
        for (int i = 0; i < 2 * numPlayers; i++) {
            for (int j = 0; j < 4; j++) {cardValues[i * 4 + j] = j + 1;}
        }
        shuffleCards(cardValues);
        try {
            FileWriter myWriter = new FileWriter(fileName + ".txt");
            for (int i = 0; i < 2 * numPlayers; i++) {

                for (int j = 0; j < 4; j++) {myWriter.write(String.valueOf(cardValues[i * 4 + j])); myWriter.write(System.lineSeparator()); }
            }



            myWriter.close();  // must close manually
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Integer [] loadPack(String filePath, int playerCount) throws IOException {
        Integer[] Pack = new Integer[8 * playerCount];

        int counter = 0;

        // Reads in cards from file.
        try (Scanner myReader = new Scanner(new File(filePath + ".txt"))) {
            while (myReader.hasNextLine()) {
                String CurrentLine = myReader.nextLine().trim(); // correction, to read and update all values in loadpack rather than one, counter didnt increment.
                // better error handling added.

                if (CurrentLine.equals("")) { // found an empty line, throw exec
                    Exception EmptyLineException = new Exception();
                    HandleException.EmptyLineException(EmptyLineException, filePath + ".txt", counter);
                }

                int Denomination = -1;

                try {
                    Denomination = Integer.parseInt(CurrentLine);

                } catch (NumberFormatException e) {
                    HandleException.InvalidDenominationException(e, CurrentLine);
                }

                if (Denomination < 0) {
                    Exception InvalidNumberException = new NumberFormatException();
                    HandleException.InvalidDenominationException(InvalidNumberException, CurrentLine);

                }

                if (counter >= Pack.length) {
                    Exception IllegalPackSizeException = new IllegalArgumentException();
                    HandleException.InvalidPackSizeException(IllegalPackSizeException, counter);

                }

                Pack[counter] = Denomination;
                counter++;

            }
        }

        if (counter == Pack.length) {
            return Pack;
        } else {
            Exception InvalidNumberException = new NumberFormatException();
            HandleException.InvalidPackSizeException(InvalidNumberException, counter);
            return null;
        }
    }

    // Takes integer to use collection shuffle method.
     Integer [] shuffleCards(Integer[] cards) {
        List<Integer> l = Arrays.asList(cards);
        Collections.shuffle(l);
        cards = l.toArray(new Integer[0]);
        return cards;
    }


    // Helper reads pack file, distributing Cards to Players and CardDecks.

    /* modifications
    // the dealing order here was wrong, 4 cards to each player first then the decks
    // Players should start at index 1, not 0
    // each player needs a previous and next deck where next deck is numbered as n where n is the player number
    // constructor needs more arguments
     */
    Player[] distributePack(String packFileName, int playerCount) throws IOException {
        Player[] players = new Player[playerCount];
        cardDecks = new CardDeck[playerCount];
        Integer [] cards = shuffleCards(loadPack(packFileName, playerCount));


        // Keeps track of cards in card array when adding to objects.
        int cardCounter = 0;
        ArrayList<ArrayList<Card>> AllPlayerHands = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            // foreach player in game
            ArrayList<Card> PlayerHand = new ArrayList<>();
            for (int x = 0; x < 4; x++) {
                // for each card in hand

                PlayerHand.add(new Card(cards[cardCounter])); // construct a new card from int[n] cards
                cardCounter++;
            }

            AllPlayerHands.add(PlayerHand); // add player hand to all player hands

        }

        for (int j = 0; j < playerCount; j++) {
            Stack<Card> deckCards = new Stack<>();
            // foreach deck in game
            for (int y = 0; y < 4; y++ ) {
                // foreach card in deck
                //System.out.println(cardCounter);
                Card NewCard = new Card(cards[cardCounter]);
                deckCards.add(NewCard); // construct a new card from the int[n] cards array
                cardCounter++;
            }

            cardDecks[j] = new CardDeck(j + 1, deckCards);
        }

        for (CardDeck Deck : cardDecks) {
            //System.out.println("[DEBUG] Deck: " + Deck.GetDeckNumber() + " initialised with " + Deck.GetDeckSize() + " Cards");

        }


        /*for (int i = 0; i < 2 * playerCount; i++) {

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
        }*/
        // Used to create threads for players.
        return InitialisePlayers(playerCount, AllPlayerHands, cardDecks); // initialise the players and return
    }

    public static void main(String[] args) {
        CardGame game = new CardGame();

        System.out.println("Welcome to Game.CardGame\n");


        Scanner Input = new Scanner(System.in);
        int plrCount = 0;

        while (plrCount < 2) {
            System.out.println("Please input the number of players:");
            String NumberOfPlayers = Input.nextLine();
            //System.out.println("Please input the number of players:");
            // Converts number of players to int, handles type and value exceptions.
            try {
                plrCount = Integer.parseInt(NumberOfPlayers);

            } catch (Exception ex) {
                // most likely user didn't enter a number
                HandleException.InvalidInputExceptionNumberFormat(ex);
                System.out.println("Please input a whole number");
                plrCount = 0;
            }

            if (plrCount < 2) {
                // throw exception. at least two players are required to start the game
                Exception InvalidPlayerCountException = new IllegalArgumentException();
                HandleException.InvalidPlayerCountException(InvalidPlayerCountException, plrCount);
            }
        }


        /*System.out.println("Please input the card pack filename:");
        String packName = Input.nextLine();
        try (Scanner myReader = new Scanner(new File(packName + ".txt"))) {
        } catch (FileNotFoundException e) {
            System.out.println("Please input a valid filename:");
            e.printStackTrace();
        }*/


        boolean Decided = false;
        Player[] Players = null;

        while(!Decided) {
            System.out.println("Would you like to generate a pack file for the game or would you like to provide one? enter 1 for autogen or 2 to provide a file");
            String Decision = Input.nextLine();
            if(Decision.equals("1")) {
                // auto generate a pack file
                System.out.println("[AUTOGENERATING PACK FILE]");
                game.generatePack(plrCount, "Generatedpack");
                try {
                    Players = game.distributePack("Generatedpack", plrCount);
                    Decided = true;
                    break;
                } catch (IOException e) {
                    HandleException.InvalidPackSizeException(e, plrCount);
                }

            } else if (Decision.equals("2")) {
                // try to find the provided pack file
                while (Players == null) {
                    System.out.println("Please input the name (without extension) of the card pack file, make sure it is located in the same directory as this jar file.");
                    String cardPackName = Input.nextLine();

                    try {
                        Players = game.distributePack(cardPackName, plrCount);
                        Decided = true;
                        break;
                    } catch (IOException | IllegalArgumentException exc) {
                        HandleException.FileNotFoundException(exc);
                        System.out.println("Please input a valid filename.");

                    }

                }
            } else {
                System.out.println("Please choose from either option 1 to auto generate or 2 to provide a pack file (must be located in the same directory as the executable.");
            }
        }



        Thread[] PlayerThreads = createPlayerThreads(plrCount, Players);

        for (Thread t : PlayerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("The game has been interrupted.");
                return;
            }
        }

        //game.generatePack(plrCount, packName);
        //Player[] players = game.distributePack(packName, plrCount); // returns players to start threads
        // hread[] PlayerThreads = createPlayerThreads(plrCount, players);


        //* TODO: add end condition triggered by thread, writes to deck files and outputs */
        // added

        // all player threads have finished, cleanup
        game.Cleanup();

    }

    public void Cleanup() {
        // when the game has finished, cleanup and write to output files

        int WinningPlrUUID = PlayerUUIDWon.get(); // get the winning player

        // write logs
        for (Player plr : players) {
            plr.StreamLogsToOutputFile(WinningPlrUUID);
        }


        for (CardDeck deckofcards : cardDecks) {
            String FileName = "deck" + deckofcards.GetDeckNumber() + "_output.txt";

            try (FileWriter Writer_ = new FileWriter(FileName)) {
                Writer_.write("deck" + deckofcards.GetDeckNumber() + " contents:");

                for (Card card : deckofcards.GetCurrentCards()) {
                    Writer_.write(" " + card.GetCardDenomination());

                }

                Writer_.write(System.lineSeparator());

            } catch (IOException e) {
                HandleException.WriteStreamIOException(e);

            }
        }

        System.out.println("The game has been terminated.");
        System.exit(0); // 0, OK

    }

}
