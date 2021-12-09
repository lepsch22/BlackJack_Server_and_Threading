import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Server logic class, or better known as our threading class.
 */
public class ServerLogic implements Runnable{
    /**
     * Input stream from client to server
     */
    private ObjectInputStream input; // input stream from client
    /**
     * output stream from server to client
     */
    private ObjectOutputStream output; // output stream to client
    /**
     * Creat the socket for the serer and client connection
     */
    private Socket connection;
    /**
     * Status of the connection
     */
    private boolean connectionStatus;
    /**
     * The connection number.
     */
    private int counter;
    /**
     * Server that is passed into the thread
     */
    private BlackJackServer server;

    /**
     * Constructor for thread
     * @param connection connection for the thread to client
     * @param server the main server that client is running on
     * @param counter id of the thread
     */
    public ServerLogic(Socket connection,BlackJackServer server,int counter){
        this.server = server;
        this.connection = connection;
        connectionStatus = true;
        this.counter = counter;
    }

    /**
     * The thread run method. assigns the streams and then waits to draw cards for the client.
     * Once client dissconnects we terminate the connection
     */
    @Override
    public void run() {
        try {
            getStreams();
            processConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            server.setConnectionNum(server.getConnectionNum() - 1);
            server.clearConnection(counter);
            closeConnection();
        }

    }

    /**
     * draw cards method.
     * @throws IOException in case the drawing cards methodhas IOException
     */
    private void processConnection() throws IOException {

        do {
            try // read message and display it
            {
                Deck gameDeck;
                gameDeck = (Deck) input.readObject();//Grab the gamedeck from the client
                //System.out.println("TEST");
                Deck temp = new Deck();
                temp.draw(gameDeck);
                sendDeck(temp);
                //Deck temp = new Deck();//Create a temp deck that we will send the client


            } catch (ClassNotFoundException classNotFoundException) {
                System.out.println("\nUnknown object type received");
            }catch(EOFException e){
                //System.out.println("Server terminate connection.");
                connectionStatus = false;
            }
        }while (connectionStatus);


    }

    /**
     * Send the card that was drawn back to the client
     * @param playerDeck the deck of the client
     */
    private void sendDeck(Deck playerDeck) {
        try // send object to client
        {
            //drawDeck = input.readObject();
            output.writeObject(playerDeck.getCard(0));
            output.flush(); // flush output to client
            //System.out.println("\nSERVER>>> " + "You draw a: "+playerDeck.getCard(playerDeck.deckSize()-1).toString());
        } catch (IOException ioException) {
            System.out.println("\nError writing object");
        }
    }

    /**
     * Assign the streams to our instance variables
     * @throws IOException any issues assigning the steams will throw this exception
     */
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(connection.getInputStream());

        System.out.println("\nGot I/O streams");
    }

    /**
     * Method to close the connection with the thread and client.
     */
    private void closeConnection() {
        System.out.println("\nTerminating connection with "+counter);
        //setTextFieldEditable(false); // disable enterField

        try {
            output.close(); // close output stream
            input.close(); // close input stream
            connection.close(); // close socket
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
