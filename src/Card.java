import java.io.Serializable;

/**
 * This class is to create the card objects we implement serializable so they can be passed through the streams of the client and server
 */
public class Card implements Serializable {
    /**
     * Suit of card
     */
    private  Suit suit;
    /**
     * Value of card
     */
    private Value value;

    /**
     * Create a card constructor
     * @param suit suit of card
     * @param value value of card
     */
    public Card(Suit suit, Value value){
        this.value = value;
        this.suit = suit;
    }

    /**
     * Way to print out cards
     * @return card format
     */
    @Override
    public String toString(){
        return value.toString() + " of " + suit.toString();
    }

    /**
     * Value of the card
     * @return value of card
     */
    public Value getValue(){
        return value;
    }
}
