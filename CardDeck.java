import java.io.FileWriter;
import java.io.IOException;

public class CardDeck {
    private Card[] deck;
    private int number;
    private int top;
    private int tail;

    public CardDeck(int number) {
        // The size is set to 13 to allow for the case where one player
        // discards all from its hand and the previous deck to the following deck
        // 13 allows for 12 cards to be in the deck at one time
        deck = new Card[13];
        this.number = number;
        top = 0;
        tail = 0;
    }

    public int getNumber() {
        return number;
    }

    public synchronized void addCard(Card card) {
        deck[tail++] = card;
        tail = tail % deck.length;
        notify();
    }

    public synchronized Card drawCard() throws IllegalStateException {
        if (top == tail) {
            try{
                wait();
            } catch (InterruptedException e){}
        }
        Card c = deck[top++];
        top = top % deck.length;
        return c;
    }

    public Boolean isEmpty(){
        return top == tail;
    }

    public void printDeckToFile() {
        try (FileWriter file = new FileWriter("deck" + number + "_output.txt")) {
            String toWrite = "deck" + number + " contents ";
            for (int i = 0; i < deck.length; i++) {
                if (deck[i] != null) {
                    toWrite += deck[i].getValue() + " ";
                }
            }
            file.write(toWrite);
        } catch (IOException e) {
            System.out.println("fail");
        }
    }
}
