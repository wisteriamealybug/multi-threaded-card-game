import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends Thread {
    private Card[] hand;
    private int number;
    private int wanted;
    private CardDeck drawDeck;
    private CardDeck discardDeck;
    private volatile AtomicInteger winningPlayer;
    private CyclicBarrier latch;

    public Player(int number, CardDeck drawDeck, CardDeck discardDeck,
            AtomicInteger winningPlayer, CyclicBarrier latch) {
        hand = new Card[4];
        wanted = 0;
        this.number = number;
        setName("player" + number);
        this.winningPlayer = winningPlayer;
        this.drawDeck = drawDeck;
        this.discardDeck = discardDeck;
        this.latch = latch;
    }

    public void run() {
        String initalText = "player " + number + " initial hand " + getHand() + "\n";
        String finalText = "";
        writeToFile(initalText, false);

        if (hasWon()) {
            winningPlayer.set(number);
            finalText += "player " + number + " wins\n";
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
        } catch (BrokenBarrierException e) {
        }

        while (winningPlayer.get() == -1) {
            atomicDrawDiscard();
            if (hasWon()) {
                winningPlayer.set(number);
                finalText += "player " + number + " wins\n";
                discardDeck.notifyHasWon();
            }
        }
        if (!hasWon()) {
            int winningNumber = winningPlayer.get();
            finalText += "player " + winningNumber + " has informed player " + number + " that player " + winningNumber
                    + " has won\n";
            discardDeck.notifyHasWon();
        }
        finalText += "player " + number + " exits\n" + "player " + number + " final hand " + getHand();
        writeToFile(finalText, true);
        drawDeck.printDeckToFile();
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
        Card drawn = null;
        Card toDiscard = null;
        // Player is interrupted if another player wins while it waits for an empty deck
        // It then returns, failing the atomic action entirely.
        try {
            drawn = drawDeck.drawCard();
        } catch (InterruptedException e) {
        }
        if (drawn == null) {
            return;
        }
        toDiscard = discard();
        addCard(drawn);
        discardDeck.addCard(toDiscard);

        writeToFile(
                "player " + number + " draws a " + drawn.getValue() + " from deck " + drawDeck.getNumber() +
                        "\nplayer " + number + " discards a " + toDiscard.getValue() + " to deck "
                        + discardDeck.getNumber() +
                        "\nplayer " + number + " current hand " + getHand() + "\n",
                true);
        drawDeck.printDeckToFile();
        discardDeck.printDeckToFile();
    }

    private void writeToFile(String textToWrite, boolean append) {
        try (FileWriter file = new FileWriter("Output/" + getName() + "_output.txt", append);) {
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

    public boolean hasWon() {
        return hand[0].getValue() == hand[1].getValue() && hand[1].getValue() == hand[2].getValue()
                && hand[2].getValue() == hand[3].getValue();
    }

    private String getHand() {
        String hand = "";
        for (int i = 0; i < 4; i++) {
            hand += this.hand[i].getValue() + " ";
        }
        return hand.strip();

    }
}
