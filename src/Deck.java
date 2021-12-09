import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Deck class is where most of game functions are created.
 */
public class Deck implements Serializable {
    /**
     * Make the deck an array of cards
     */
    private ArrayList<Card> totalCards;

    /**
     * COnstructor for the deck
     */
    public Deck(){
        totalCards = new ArrayList<Card>();
    }

    /**
     * Remove on of the cards from the deck at index
     * @param index where to remove card
     */
    public void remove(int index){
        totalCards.remove(index);
    }

    /**
     * Return the card at given index
     * @param index given index of card
     * @return card format
     */
    public Card getCard(int index){
        return totalCards.get(index);
    }

    /**
     * Add a card to the deck used for dealer and player decks
     * @param card card of player
     */
    public void add(Card card){
        totalCards.add(card);
    }

    /**
     * Creat a deck of 4 sets of 52 cards
     */
    public void createNewDeck(){
        for (int i = 0; i < 4; i++) {
            for (Suit cardSuit : Suit.values()) {
                for (Value cardValue : Value.values()) {
                    Card newCard = new Card(cardSuit, cardValue);
                    totalCards.add(newCard);
                }
            }
        }
    }

    /**
     * Size of deck
     * @return the size of the deck
     */
    public int deckSize(){
        return totalCards.size();
    }

    /**
     * Shuffle the deck
     */
    public void shuffle(){
        Collections.shuffle(totalCards);

    }

    /**
     * draw a card from the deck
     * @param drawDeck deck to draw from
     */
    public void draw(Deck drawDeck){
        totalCards.add(drawDeck.getCard(0));
        drawDeck.remove(0);
    }

    /**
     * How the cards are valued
     * @return value of cards
     */
    public int getValue(){
        int totalValue = 0;
        int ace = 0;
        for(Card card: totalCards){
            Value value = card.getValue();
            switch(value){
                case TWO: totalValue+=2;
                    break;
                case THREE: totalValue+=3;
                    break;
                case FOUR: totalValue+=4;
                    break;
                case FIVE: totalValue+=5;
                    break;
                case SIX: totalValue+=6;
                    break;
                case SEVEN: totalValue+=7;
                    break;
                case EIGHT: totalValue+=8;
                    break;
                case NINE: totalValue+=9;
                    break;
                case TEN: totalValue+=10;
                    break;
                case JACK: totalValue+=10;
                    break;
                case KING: totalValue+=10;
                    break;
                case QUEEN: totalValue+=10;
                    break;
                case ACE: ace+=1;
                    break;
            }
        }
        for (int i = 0; i < ace; i++) {
            if(totalValue > 10){
                totalValue += 1;
            }
            else{
                totalValue += 11;
            }

        }
        return totalValue;
    }

    /**
     * clear deck
     */
    public void clear(){
        totalCards.clear();
    }

    /**
     * print out the deck in a certain format
     * @return the cards in deck.
     */
    @Override
    public String toString(){
        String stringBuilder = "";
        int cardCount = 0;
        for (Card card: totalCards){
            stringBuilder += "\n"+ card.toString();
            cardCount++;
        }
        return stringBuilder;
    }

}
