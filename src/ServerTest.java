import javax.swing.*;

/**
 * This is the class to start the server THIS MUST BE STARTED FIRST for clients to connect.
 */
public class ServerTest {
    public static void main(String[] args) {
        BlackJackServer application = new BlackJackServer(); // create server
        application.runServer(); // run server application

    }
}
