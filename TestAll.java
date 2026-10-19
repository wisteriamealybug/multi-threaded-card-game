import org.junit.runners.Suite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.*;

@RunWith(Suite.class)
@SuiteClasses({
        TestCard.class,
        TestCardDeck.class,
        TestPlayer.class,
        TestCardGame.class
})
public class TestAll {
}
