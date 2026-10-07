public class CardDeck {
    Card[] deck;
    int top;
    int tail;

    public CardDeck(int n){
        deck = new Card[2*n];
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
}
