import java.io.File;
import java.util.Scanner;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TestPlayer {
    AtomicBoolean a = new AtomicBoolean(false);
    AtomicInteger i = new AtomicInteger(-1);

    String data;

    CardDeck deck1;
    CardDeck deck2;

    @Before
    public void setUp(){
        deck1 = new CardDeck(1);
        deck2 = new CardDeck(2);
        for (int i = 0; i < 4; i++){
            deck1.addCard(new Card(deck1.getNumber()));
            deck2.addCard(new Card(deck2.getNumber()));
        }
    }
    @After
    public void tearDown(){
        deck1 = null;
        deck2 = null;
    }

    @Test 
    public void TestAtomicDrawDiscard(){
        Player p = new Player(1, deck1, deck2, a, i);
        for (int i = 0; i < 5; i++){
            p.addCard(new Card(0));
        }
        for (int j = 1; j < 5; j++){
            p.atomicDrawDiscard();
            File f = new File("player1_output.txt");
            try (Scanner reader = new Scanner(f)){
                while (reader.hasNextLine()){
                    data = reader.nextLine();
                }
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }
            String[] splitData = data.split(" ");
            int count = 0;
            for (int i = splitData.length-1; i > splitData.length-5; i--){
                if (splitData[i].equals("1")){
                    count++;
                }
            }
            assertEquals("Expected " + j + " cards with value 1.", j, count);
        }
    }

    @Test 
    public void TestWaitNotify(){

        CardDeck emptyDeck = new CardDeck(1);
        Player p1 = new Player(1, emptyDeck, deck2, a, i);
        Player p2 = new Player(2, deck2, emptyDeck, a, i);
        for (int i = 0; i < 4; i++){
            p1.addCard(new Card(0));
            p2.addCard(new Card(3));
        }

        Thread t1 = new Thread() {
            public void run(){
                p1.atomicDrawDiscard();
            }
        };
        Thread t2 = new Thread(){
            public void run(){
                p2.atomicDrawDiscard();
            }
        };
        t1.start();
        t2.start();

        try {
            t1.join();
        } catch (InterruptedException e){}

        File f = new File("player1_output.txt");
        try (Scanner reader = new Scanner(f)){
            while (reader.hasNextLine()){
                data = reader.nextLine();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        String[] splitData = data.split(" ");
        int count = 0;
        for (int i = splitData.length-1; i > splitData.length-5; i--){
            if (splitData[i].equals("3")){
                count++;
            }
        }

        assertEquals(1, count);
    }
}