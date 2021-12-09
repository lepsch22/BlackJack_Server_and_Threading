// Fig. 28.5: Client.java
// Client portion of a stream-socket connection between client and server.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class for server.
 */
public class BlackJackClient{
    /**
     * Output stream to server
     */
    private ObjectOutputStream output; // output stream to server
    /**
     * stream from server to client
     */
    private ObjectInputStream input; // input stream from server
    /**
     * Host
     */
    private String chatServer; // host server for this application
    /**
     * client socket
     */
    private Socket client; // socket to communicate with server

    // initialize chatServer and set up GUI

    /**
     *
     * @param host
     */
    public BlackJackClient(String host) {
        chatServer = host; // set server to which this client connects
    }

    // connect to server and process messages from server

    /**
     * Run the client to try to connect to the server. This will also be where we call the logic and assign the streams.
     */
    public void runClient() {
        try // connect to server, get streams, process connection
        {
            connectToServer(); // create a Socket to make connection
            getStreams(); // get the input and output streams
            processConnection(); // process connection
        } catch (EOFException eofException) {
            System.out.println("\nClient terminated connection");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeConnection(); // close connection
        }
    }

    // connect to server

    /**
     * Try to connect to the server, and creat the socket for the client
     * @throws IOException If there is an issue connecting to server
     */
    private void connectToServer() throws IOException {
        System.out.println("Attempting connection\n");

        // create Socket to make connection to server
        //client = new Socket(InetAddress.getByName(chatServer), 12345);
        client = new Socket("127.0.0.1", 23735);

        // display connection information
        System.out.println("Connected to: " +
                client.getInetAddress().getHostName());
    }

    // get streams to send and receive data

    /**
     * Assign the stream to get the client and server streams
     * @throws IOException
     */
    private void getStreams() throws IOException {
        // set up output stream for objects
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer to send header information

        // set up input stream for objects
        input = new ObjectInputStream(client.getInputStream());

        System.out.println("\nGot I/O streams\n");
    }

    // process connection with server

    /**
     * Blackjack game logic, when drawin cards we will call fro the server.
     * @throws IOException
     */
    private void processConnection() throws IOException {

        //Game Start
        Client test = new Client(50,1);
        Scanner userIn =  new Scanner(System.in);

        double playerMoney = test.getClientMoney();

        //Scanner userIn = new Scanner(System.in);
        System.out.println("Welcome to the game of blackjack, "+test+".\n");

        Deck deck = new Deck();
        deck.createNewDeck();
        deck.shuffle();

        Deck playerDeck = new Deck();
        Deck dealerDeck = new Deck();

        do // process messages sent from server
        {
            try // read message and display it
            {



                boolean endRound = false;
                //test.setClientDeck(playerDeck);
                double bet = -1;
                while (bet <= 0 || bet > playerMoney) {
                    System.out.println(test + " total money: " + playerMoney + ", how much do you want to bet?");
                    bet = userIn.nextDouble();
                    if (bet > playerMoney) {
                        System.out.println(test + " does not have that much money!");
                    } else if (bet <= 0) {
                        System.out.println(test + " must enter a bet greater than 0.");
                    }
                }

                System.out.println(test + " successfully bet " + bet + " dollars.");


                //Draw two cards for the player
                sendDeck(deck);//Send the deck to the server
                playerDeck.add((Card) input.readObject());// Get the card the server has drawn
                sendDeck(deck);
                playerDeck.add((Card) input.readObject());
                //System.out.println(playerDeck);

                //Draw two cards for the dealer.
                sendDeck(deck);//Send the deck to the server
                dealerDeck.add((Card) input.readObject());// Get the card the server has drawn
                sendDeck(deck);
                dealerDeck.add((Card) input.readObject());
                //System.out.println(dealerDeck);

                while (true) {
                    System.out.println(test + " hand: " + playerDeck);
                    System.out.println(test + " deck has a value of: " + playerDeck.getValue() + "\n");

                    System.out.println("Dealer has: " + dealerDeck.getCard(0));

                    System.out.println("Would " + test + " like to 1.Hit or 2.Stand");
                    int in = userIn.nextInt();
                    //HIT
                    if (in == 1) {
                        sendDeck(deck);//Send the deck to the server
                        playerDeck.add((Card) input.readObject());// Get the card the server has drawn
                        System.out.println(test + " drew a " + playerDeck.getCard(playerDeck.deckSize() - 1).toString());

                        if (playerDeck.getValue() > 21) {
                            System.out.println("BUST! " + test + " drew a " + playerDeck.getValue() + ". " + test + " loses " + bet + " dollars to the dealer.");
                            playerMoney = playerMoney - bet;
                            endRound = true;
                            break;
                        } else if (playerDeck.getValue() == 21) {
                            System.out.println("BLACKJACK!");
                            playerMoney = playerMoney + bet;
                            endRound = true;
                            break;

                        }
                    } else if (in == 2) {
                        System.out.println("You stand. ");
                        break;

                    }
                }
                //Reveal dealer
                System.out.println("Dealer cards: "+ dealerDeck);
                //Does the dealer have more points than the player? When the player stands
                if((dealerDeck.getValue() >= 17) && dealerDeck.getValue() > playerDeck.getValue() && !endRound){
                    System.out.println("The dealer beats "+test+ " With a value of "+dealerDeck.getValue() + ". "+test+"value was "+playerDeck.getValue() + ". "+test+" lose "+bet + " dollars.");
                    playerMoney = playerMoney - bet;
                    endRound = true;
                }

                while(dealerDeck.getValue() < 17  && !endRound){
                    sendDeck(deck);//Send the deck to the server
                    dealerDeck.add((Card) input.readObject());// Get the card the server has drawn
                    System.out.println("Dealer draws: " +dealerDeck.getCard(dealerDeck.deckSize()-1).toString());
                }
                //Dealer value
                System.out.println("Dealer has: "+dealerDeck.getValue());
                //Dealer busted?
                if(dealerDeck.getValue() > 21 && !endRound){
                    System.out.println("The dealer busted! Dealer had a value of "+dealerDeck.getValue() + ". "+test+" value was "+playerDeck.getValue() + ". "+test+" lose "+bet + " dollars.");
                    playerMoney += bet;
                    endRound = true;
                }
                //Tie?
                else if((playerDeck.getValue() == dealerDeck.getValue()) && !endRound){
                    System.out.println("You tie with the dealer! Dealer had a value of "+dealerDeck.getValue() + ". "+test+" value was "+playerDeck.getValue() + ". "+test+" gets "+bet + " dollars back.");
                    endRound = true;
                }
                //win?
                else if(playerDeck.getValue() > dealerDeck.getValue() && !endRound){
                    System.out.println(test+" beat the the dealer! Dealer had a value of "+dealerDeck.getValue() + ". "+test+" value was "+playerDeck.getValue() + ". "+test+" lose "+bet + " dollars.");
                    playerMoney += bet;
                    endRound = true;
                }
                else if(dealerDeck.getValue() > playerDeck.getValue() && dealerDeck.getValue() <= 21 &&!endRound) {
                    System.out.println("The dealer beats "+test+ " With a value of "+dealerDeck.getValue() + ". "+test+" value was "+playerDeck.getValue() + ". "+test+" lose "+bet + " dollars.");
                    playerMoney -= bet;
                    endRound = true;

                }
                test.setClientMoney(playerMoney);

                playerDeck.clear();
                dealerDeck.clear();



            } catch (ClassNotFoundException classNotFoundException) {
                System.out.println("\nUnknown object type received");
            }

        } while (test.getClientMoney() > 0);
        System.out.println(test+" are out of money, better luck next time!");
    }

    // close streams and socket

    /**
     * Close the socket and the streams
     */
    private void closeConnection() {
        System.out.println("\nClosing connection");
        //setTextFieldEditable(false); // disable enterField

        try {
            output.close(); // close output stream
            input.close(); // close input stream
            client.close(); // close socket
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // send message to server

    /**
     * This is how we send the deck to draw from to the server
     * @param drawDeck deck tod raw from
     */
    private void sendDeck(Deck drawDeck) {
        try // send object to client
        {
            //drawDeck = input.readObject();
            output.writeObject(drawDeck);
            output.flush(); // flush output to server
            //System.out.println("\nCLIENT>>> " + "Drawing card");
        } catch (IOException ioException) {
            System.out.println("\nError writing object");
        }
    }


}

/**************************************************************************
 * (C) Copyright 1992-2014 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
