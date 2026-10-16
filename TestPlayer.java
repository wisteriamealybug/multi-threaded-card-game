import java.io.File;
import java.util.Scanner;
import java.io.IOException;

import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class TestPlayer {
    AtomicInteger i = new AtomicInteger(-1);

    @AfterClass
    public static void removeFiles(){
        File f = new File("Output/player1_output.txt"); 
        f.delete();
        f = new File("Output/player2_output.txt"); 
        f.delete();
        f = new File("Output/deck1_output.txt"); 
        f.delete();
        f = new File("Output/deck2_output.txt"); 
        f.delete();
    }

    @Test 
    public void testAtomicDrawDiscard(){
        String data = "";
        CardDeck deck1 = new CardDeck(1, 2);
        CardDeck deck2 = new CardDeck(2, 2);
        for (int i = 0; i < 4; i++){
            deck1.addCard(new Card(1));
            deck2.addCard(new Card(2));
        }
        CyclicBarrier latch = new CyclicBarrier(1);
        Player p = new Player(1, deck1, deck2, i, latch);
        for (int i = 0; i < 5; i++){
            p.addCard(new Card(0));
        }
        for (int j = 1; j < 5; j++){
            p.atomicDrawDiscard();
            File f = new File("Output/player1_output.txt");
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
    public void testWaitNotify(){
        String data = "";
        CardDeck emptyDeck = new CardDeck(1, 2);
        CardDeck deck2 = new CardDeck(2, 2);
        for (int i = 0; i< 4; i++){
            deck2.addCard(new Card(2));
        }
        final CyclicBarrier latch = new CyclicBarrier(1);
        Player p1 = new Player(1, emptyDeck, deck2, i, latch);
        Player p2 = new Player(2, deck2, emptyDeck, i, latch);
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

        File f = new File("Output/player1_output.txt");
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