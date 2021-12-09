// Fig. 28.3: Server.java
// Server portion of a client/server stream-socket connection. 


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class where the server is set up.
 */
public class BlackJackServer {
    /**
     * Server socket to create the server
     */
    private ServerSocket server; // server socket
    /**
     * Worker is the threads that will handle logic.
     */
    private ArrayList<ServerLogic> worker;
    /**
     * number of connections, only 2 are allowed at a time.
     */
    private int connectionNum;

    // set up GUI

    /**
     * BlackJack server constructor
     */
    public BlackJackServer() {
    }

    /**
     * Clear connection to server.
     * @param connectionNum what connection are they?
     */
    public void clearConnection(int connectionNum){
        worker.set(connectionNum,null);
        //connection.remove(connectionNum);
    }

    /**
     * what is the connection number of the server.
     * @return
     */
    public int getConnectionNum() {
        return connectionNum;
    }

    /**
     * Set the connection number for the client
     * @param connectionNum connection position
     */
    public void setConnectionNum(int connectionNum) {
        this.connectionNum = connectionNum;
    }

    /**
     * Setup the server and make 2 threads to handle two client connections to the server.
     */
    // set up and run server
    public void runServer() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        worker = new ArrayList<>(2);
        worker.add(null);
        worker.add(null);
        connectionNum = 0;
        System.out.println("SERVER STARTUP");
        try // set up server to receive connections; process connections
        {
            server = new ServerSocket(23735, 100); // create ServerSocket

            while (true) {
                //System.out.println(worker.get(0));//Code breaks without this
                //System.out.println(worker.get(1));
                //while (worker.get(0) == null || worker.get(1) == null){
                    try {
                        waitForConnection(); // wait for a connection
                        executorService.execute(worker.get(connectionNum));
                        connectionNum ++;

                    } catch (EOFException eofException) {
                        System.out.println("\nServer terminated connection");
                    }
                //}
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // wait for connection to arrive, then display connection info

    /**
     * Wait for the client connection once we establish a connection assign the connection to one of the workers.
     * @throws IOException
     */
    private void waitForConnection() throws IOException {
        System.out.println("Waiting for connection\n");
        Socket test;
        test = server.accept(); // allow server to accept connection

        System.out.println("Test "+connectionNum);
        int temp = connectionNum;
        worker.set(connectionNum,new ServerLogic(test,this,temp));
        //connection.add(test);
        //Create the thread put the thread into the thread pool
        //Pass the socket into the runnable class
        System.out.println("Connection " + connectionNum + " received from: " +
                test.getInetAddress().getHostName());
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