import java.io.FileWriter;
import java.io.IOException;

public class CardDeck {
    private Card[] deck;
    private int number;
    private int top;
    private int tail;

    public CardDeck(int n, int number){
        deck = new Card[2*n];
        this.number = number;
        top = 0;
        tail = 0;
    }

    public void addCard(Card card){
        deck[tail++] = card;
        tail = tail % deck.length;
    }

    public Card drawCard() throws IllegalStateException {
        if (top == tail){
            throw new IllegalStateException("Cannot draw card from empty deck");
        } else{
            Card c = deck[top++];
            top = top % deck.length;
            return c;
        }
    }

    public void printDeckToFile(){
        try {
            FileWriter file = new FileWriter("deck" + number + "_output.txt");
            String toWrite = "deck" + number + " contents ";
            for (int i = 0; i < deck.length; i++){
                if (deck[i] != null){
                    toWrite += deck[i].getValue() + " ";
                }
            }
            file.write(toWrite);
            file.close();
        } catch (IOException e){System.out.println("fail");}
    }
}
