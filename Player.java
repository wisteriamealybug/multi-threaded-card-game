import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends Thread {
    private Card[] hand;
    private int number;
    private int wanted;
    private CardDeck drawDeck;
    private CardDeck discardDeck;
    private volatile AtomicBoolean hasPlayerWon;
    private volatile AtomicInteger winningPlayer;

    public Player(int number, CardDeck drawDeck, CardDeck discardDeck, AtomicBoolean flag, AtomicInteger winningPlayer) {
        hand = new Card[4];
        wanted = 0;
        this.number = number;
        setName("player" + number);
        this.hasPlayerWon = flag;
        this.winningPlayer = winningPlayer;
        this.drawDeck = drawDeck;
        this.discardDeck = discardDeck;
    }

    public void run() {
        String initalText = "player " + number + " initial hand " + getHand();
        String finalText = "";
        writeToFile(initalText);

        if (hasWon()) {
            winningPlayer.set(number);
            hasPlayerWon.set(true);
            finalText += "player " + number + " wins\n";
        }

        while (!hasPlayerWon.get()) {
            atomicDrawDiscard();
            if (hasWon()) {
                winningPlayer.set(number);
                hasPlayerWon.set(true);
                finalText += "player " + number + " wins\n";
            }
        }
        if (!hasWon()) {
            int winningNumber = winningPlayer.get();
            finalText += "player " + winningNumber + " has informed player " + number + " that player" + winningNumber + " has won\n";
        }
        finalText += "player " + number + " exits\n" + "player " + number + " final hand " + getHand();
        writeToFile(finalText);
    }

    public void addCard(Card card) {
        for (int i = 0; i < 4; i++) {
            if (hand[i] == null) {
                if (card.getValue() == number) {
                    wanted++;
                }
                hand[i] = card;
                return;
            }
        }
    }

    public synchronized void atomicDrawDiscard() {
        Card drawn = drawDeck.drawCard();
        Card toDiscard = discard();
        addCard(drawn);
        discardDeck.addCard(toDiscard);

        writeToFile(
                "player " + number + " draws a " + drawn.getValue() + " from deck " + drawDeck.getNumber() +
                        "\n player " + number + " discards a " + toDiscard.getValue() + " to deck " + discardDeck.getNumber() +
                        "\n player " + number + " current hand " + getHand());
    }

    private void writeToFile(String textToWrite) {
        try (FileWriter file = new FileWriter(getName() + "_output.txt");) {
            file.write(textToWrite);
        } catch (IOException e) {
            System.out.println("fail");
        }
    }

    private Card discard() {
        Card c = null; // Should never return null
        int randomUnwanted = (int) Math.floor(Math.random() * (4 - wanted));
        for (int i = 0; i < 4; i++) {
            if (hand[i].getValue() != number) {
                if (randomUnwanted-- == 0) {
                    c = hand[i];
                    hand[i] = null;
                    break;
                }
            }
        }
        return c;
    }

    public Boolean hasWon() {
        return wanted == 4;
    }

    private String getHand() {
        String hand = "";
        for (int i = 0; i < 4; i++) {
            hand += this.hand[i].getValue() + " ";
        }
        return hand.strip();

    }
}
