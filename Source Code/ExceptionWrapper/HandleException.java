// this class handles exceptions and keeping them in a predictable and consistent manner
// methods are static and the class itself is a util class

// declare as package
package ExceptionWrapper;

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
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine(); // on the next line of input, quit.
        System.exit(-1);
    }

    public static void FileNotFoundException(Exception Ex) {
        System.out.println("[EXCEPTION] Card file was not found? is it located in the correct directory?");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1);
    }

    public static void NegetiveDenominationException(Exception Ex, int SerialisedCardDenomination) {
        System.out.println("[EXCEPTION] Serialised Card Denomination:"+ SerialisedCardDenomination + "from file was negetive? Should not be negetive.");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1);
    }

    public static void InvalidDenominationException(Exception Ex, String SerialisedCardDenomination) {
        System.out.println("[EXCEPTION] Serialised Card Denomination:"+ SerialisedCardDenomination + "from file is not valid");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1);
    }
    public static void InvalidPlayerCountException(Exception Ex, int PlayerCount) {
        System.out.println("[EXCEPTION] Player Count:"+ PlayerCount + " Is not valid, two or more players are required to start the game");
        for (StackTraceElement El : Thread.currentThread().getStackTrace()) {
            System.out.println(El.toString());
        }
        System.out.println("Press enter to exit...");
        new Scanner(System.in).nextLine();
        System.exit(-1);
    }
}
