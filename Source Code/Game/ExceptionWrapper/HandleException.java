// this class handles exceptions and keeping them in a predictable and consistent manner
// methods are static and the class itself is a util class

// declare as package
package Game.ExceptionWrapper;

// dependencies
import java.io.*;
import java.util.*;


public class HandleException {

    public static void InvalidInputExceptionNumberFormat(Exception Ex) {
        System.out.println("[EXCEPTION] Entered Input is not valid, please enter a number " + Ex.getMessage());
        // print the stack trace for debugging
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        // Considered fatal, quit main thread and stop execution after user conformation
        //System.out.println("Press enter to exit...");
        //new Scanner(System.in).nextLine(); // on the next line of input, quit.
        //System.exit(-1);
    }

    public static void FileNotFoundException(Exception Ex) {
        System.out.println("[EXCEPTION] file was not found? is it located in the correct directory?");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        //System.out.println("Press enter to exit...");
        //new Scanner(System.in).nextLine();
        //System.exit(-1);
    }

    public static void WriteStreamIOException(Exception Ex) {
        System.out.println("[EXCEPTION] I/O error writing to file, does the current executable have permissions to write in this directory?");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        //System.out.println("Press enter to exit...");
        //new Scanner(System.in).nextLine();
        //System.exit(-1);
    }

    public static void NegetiveDenominationException(Exception Ex, int SerialisedCardDenomination) {
        System.out.println("[EXCEPTION] Serialised Game.Card Denomination:"+ SerialisedCardDenomination + "from file was negetive? Should not be negetive.");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1);
    }

    public static void InvalidDenominationException(Exception Ex, String SerialisedCardDenomination) {
        System.out.println("[EXCEPTION] Serialised Game.Card Denomination:"+ SerialisedCardDenomination + " is not valid");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1);
    }
    public static void InvalidPlayerCountException(Exception Ex, int PlayerCount) {
        System.out.println("[EXCEPTION] Game.Player Count:"+ PlayerCount + " Is not valid, two or more players are required to start the game");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        //System.out.println("Press enter to exit...");
        //new Scanner(System.in).nextLine();
        //System.exit(-1);
    }

    public static void CardDeckIllegalRaceConditionException(Exception Ex, Thread AffectedThread) {
        System.out.println("[EXCEPTION] :: Thread Safety has failed, the player that belongs to thread " + AffectedThread.threadId() + " Has both won and had undesirable cards, this should not happen");
        for (StackTraceElement El : AffectedThread.getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-2); // -1, logic error , -2 illegalthreadstates
    }

    public static void CardObjectNotFoundException(NullPointerException Ex) {
        System.out.println("[EXCEPTION] Card Object(s) was not passed into the card deck properly, got null instead of Stack<Card>");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1); //
    }

    public static void CardNotFoundException(NullPointerException Ex) {
        System.out.println("[EXCEPTION] Card Object was not found, got null instead of Card");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1); //
    }

    public static void EmptyDeckException(Exception Ex) {
        System.out.println("[EXCEPTION] Card Object was not found, the deck is empty, got null instead of Card");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1); //
    }

    public static void IncorrectHandSizeException(Exception Ex, Thread AffectedThread) {
        System.out.println("[EXCEPTION] Hand Dealt to the player belonging to thread " + AffectedThread.threadId() + "Is not valid, either null or not the correct amount of cards.");
        for (StackTraceElement El : AffectedThread.getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1); //
    }
    public static void EmptyLineException(Exception Ex, String FileName, int LineNumber) {
        System.out.println("[EXCEPTION] File read at " + FileName + "@ :" + LineNumber + " is empty, got null instead of String");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1); //
    }

    public static void InvalidPackSizeException(Exception Ex, int Counter) {
        System.out.println("[EXCEPTION] The Pack Size is invalid, please ensure it is 8n where n is the number of players to play the game. Please try again ");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        //System.out.println("Press enter to exit...");
        //new Scanner(System.in).nextLine();
        //System.exit(-1); //
    }
}
