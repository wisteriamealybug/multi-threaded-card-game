import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

public class TestCardDeck {
    private CardDeck[] createDecks(int n) {
        CardDeck[] decks = new CardDeck[n];
        for (int i = 0; i < n; i++) {
            decks[i] = new CardDeck(i + 1);
        }
        return decks;

    }

    private void dealCards(CardDeck[] decks) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < decks.length; j++) {
                Card c = new Card(0);
                decks[j].addCard(c);
            }
        }
    }

    @Test
    public void TestNumberAssignment() {
        CardDeck[] decks = createDecks(10);
        for (int i = 0; i < 10; i++) {
            assertEquals("Deck numbers incorrectly assigned. Should be " + i + 1 + " was " + decks[i].getNumber(),
                    i + 1,
                    decks[i].getNumber());
        }
    }

    @Test
    public void TestCardAdding() {
        for (int n = 2; n < 50; n++) {
            CardDeck[] decks = createDecks(n);
            dealCards(decks);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 4; j++)
                    assertEquals(
                            "Card in deck " + i + " at position " + j + " either does not exist or is incorrectly set",
                            decks[i].drawCard().getValue(), 0);
            }
        }

    }

    @Test
    public void TestCardDrawOrder() {
        int[] numbers = new int[] { 10, 3, 7, 1 };
        CardDeck deck = new CardDeck(1);
        for (int i = 0; i < 4; i++) {
            deck.addCard(new Card(numbers[i]));
        }
        for (int i = 0; i < 4; i++) {
            int c = deck.drawCard().getValue();
            assertEquals("Card value does not match draw order", c, numbers[i]);
        }
    }

}
