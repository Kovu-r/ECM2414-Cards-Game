// Imports //

import java.io.*; // java built in I/O class
import java.util.*; // java built in utilities
import ExceptionWrapper.*; // My exception handling/wrapper utils for testing and safe cleanup

public class Main {
    // entrypoint for program
    // Handles Asking the user for the number of players and I/O to files.
    // Handles launching plr thread(s).

    public static void main(String[] args) {

        System.out.println("Started Program");

        // use the built-in scanner class in util* to read the console input

        System.out.println("Welcome to the card game, how many players will play");

        Scanner Input = new Scanner(System.in);

        String NumberOfPlayers = Input.nextLine();
        int PlrCount = 0;

        try {
            PlrCount = Integer.parseInt(NumberOfPlayers);

        } catch (Exception ex) {
            // most likely user didn't enter a number
            ExceptionWrapper.HandleException.InvalidInputExceptionNumberFormat(ex);
        }

        if (PlrCount < 2) {
            // throw exception. at least two players are required to start the game
            Exception InvalidPlayerCountException = new IllegalArgumentException();
            ExceptionWrapper.HandleException.InvalidPlayerCountException(InvalidPlayerCountException, PlrCount);
        }

        // spawn threads

        Thread[] PlayerThreads = new Thread[PlrCount];

        for(int x = 0; x < PlrCount; x++) {
            Player Plr = new Player(x);
            PlayerThreads[x] = new Thread(Plr);
            PlayerThreads[x].start();
            // start thread for new player
        }













    }

}





