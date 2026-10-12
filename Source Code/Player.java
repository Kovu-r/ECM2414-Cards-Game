// this class handles the behaviour of each player
// handles creating and managing each player's thread.
// imp runnable for thread creation

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import ExceptionWrapper.*;

public class Player implements Runnable {

    private final int InitialisationPlayerUUID;

    // basic inst
    public Player(int initialisationPlayerUUID) {
        this.InitialisationPlayerUUID = initialisationPlayerUUID;
    }

    @Override
    public void run() { // using runnable interface
        System.out.println("Player " + this.InitialisationPlayerUUID + " Started with Thread " + Thread.currentThread().threadId()); // getid() is depricated


    }
}
