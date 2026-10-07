import java.io.FileWriter;
import java.io.IOException;

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

    public void run(){
        try {
            FileWriter file = new FileWriter(getName()+"_output.txt");
            file.write("player " + number + " initial hand " + hand[0].getValue() + 
            hand[1].getValue() + hand[2].getValue() + hand[3].getValue());
            file.close();
        } catch (IOException e){System.out.println("fail");}
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
