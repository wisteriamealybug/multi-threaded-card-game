import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

public class TestCard {
    @Test(expected = IllegalArgumentException.class)
    public void testNegativefail() {
        Card c = new Card(-1);
    }

    @Test
    public void testValueGet() {
        for (int n = 0; n < 50; n++) {
            Card c = new Card(n);
            assertEquals(c.getValue(), n);
        }
    }
}
