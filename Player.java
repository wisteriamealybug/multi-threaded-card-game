public class Player extends Thread {
    private Card[] hand;
    private int number;
    private int wanted;

    public Player(int number){
        hand = new Card[4];
        wanted = 0;
        this.number = number;
        setName("player" + number);
    }

    public void addCard(Card card){
        for(int i = 0; i < 4; i++){
            if (hand[i] == null){
                if (hand[i].getValue() == number){
                    wanted++;
                }
                hand[i] = card;
                return;
            }
        }
    }

    public Card discard(){
        int randomUnwanted = (int)Math.floor(Math.random() * (4-wanted));
        for (int i = 0; i < 4; i++){
            if (hand[i].getValue() != number){
                if (randomUnwanted-- == 0){
                    Card c = hand[i];
                    hand[i] = null;
                    return c;
                }
            }
        }
        return hand[0]; // this will not cause errors do not worry
    }

    public Boolean hasWon(){
        return wanted == 4;
    }
}
