import javax.swing.*;

/**
 * Client to connect to the blackjack server. The client class has been modified to be ran multiple times to create multiple clients.
 * Only to clients can connect at a time. Any extra clients will wait in a queue
 */
public class ClientTest {
    public static void main(String[] args) {
        BlackJackClient application; // declare client application



        if (args.length == 0) {
            application = new BlackJackClient("127.0.0.1"); // connect to localhost

        }
        else {
            application = new BlackJackClient(args[0]); // use args to connect

        }

        application.runClient(); // run client application


    }
}
