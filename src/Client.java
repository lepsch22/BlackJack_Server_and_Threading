public class Client {
    /**
     * Client money will be used to keep track of the game.
     */
    private double clientMoney;
    /**
     * Not used much in the actual prgoram dont worry.
     */
    private int clientID;

    /**
     * Client just has a set of money
     * @param clientMoney client money
     * @param clientID client ID all id are the same
     */
    public Client(int clientMoney,int clientID){
        this.clientID = clientID;
        this.clientMoney = clientMoney;

    }

    /**
     * Getter for client money
     * @return client money
     */
    public double getClientMoney(){
        return clientMoney;
    }

    /**
     * Setter for client money
     * @param input client money
     */
    public void setClientMoney(double input){
        clientMoney = input;
    }


    /**
     * Print out client
     * @return format of client
     */
    @Override
    public String toString(){
        return "Client: "+clientID;
    }
}
