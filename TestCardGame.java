import java.beans.Transient;
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

public class TestCardGame {
    private static int maxPlayers = 100;

    @AfterClass
    public static void removeFiles(){
        File f;
        for (int i = 1; i <= maxPlayers; i++){
            f = new File("Output/player" + i + "_output.txt");
            f.delete();
            f = new File("Output/deck" + i + "_output.txt");
            f.delete();
        }
    }

    @Test
    public void testReadPack(){
        CardGame game = new CardGame(3);
        try{
            Card[] pack = game.readPack(3, "TestPacks/testpack1.txt");
            for (int i = 1; i < 25; i++){
                assertEquals(i, pack[i-1].getValue());
            }
        } catch (FileNotFoundException e){}
    }

    @Test (expected = FileNotFoundException.class)
    public void testPackNotFound() throws FileNotFoundException{
        CardGame game = new CardGame(3);
        Card[] pack = game.readPack(3, "TestPacks/notreal.txt");
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void testPackLong(){
        CardGame game = new CardGame(3);
        try{
            Card[] pack = game.readPack(3, "TestPacks/testinvalidpacklong.txt");
        } catch (FileNotFoundException e){}
    }

    @Test (expected = IllegalStateException.class)
    public void testPackShort(){
        CardGame game = new CardGame(3);
        try{
            Card[] pack = game.readPack(3, "TestPacks/testinvalidpackshort.txt");
        } catch (FileNotFoundException e){}
    }

    @Test (expected = IllegalArgumentException.class)
    public void testPackNegative(){
        CardGame game = new CardGame(3);
        try{
            Card[] pack = game.readPack(3, "TestPacks/testinvalidpacknegative.txt");
        } catch (FileNotFoundException e){}
    }

    @Test 
    public void testConstructor(){
        CardGame game = new CardGame(100);
        assertEquals(100, game.getPlayerNumber());
        assertEquals(-1, game.getWinner());
    }

    @Test
    public void testDeal(){
        Card[] pack = new Card[0];
        CardGame game = new CardGame(5);
        try {
            pack = game.readPack(5, "TestPacks/testpack2.txt");
        } catch (FileNotFoundException e){}
        game.deal(pack);
        Player[] players = game.getPlayers();
        CardDeck[] decks = game.getDecks();

        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 5; i++){
                try {
                    assertEquals(1, decks[i].drawCard().getValue());
                } catch (InterruptedException e){}
            }
        }
    }

    @Test 
    public void testRunImmediateWin(){
        for (int i = 0; i < 100; i++){
            Card[] pack = new Card[0];
            CardGame game = new CardGame(5);
            try {
                pack = game.readPack(5, "TestPacks/testpack3.txt");
            } catch (FileNotFoundException e){}
            game.deal(pack);
            game.runGame();

            assertEquals(2, game.getWinner());
        }
    }

    @Test 
    public void testRunP1Wins(){
        for (int i = 0; i < 100; i++){
            Card[] pack = new Card[0];
            CardGame game = new CardGame(5);
            try {
                pack = game.readPack(5, "TestPacks/testpack4.txt");
            } catch (FileNotFoundException e){}
            game.deal(pack);
            game.runGame();

            assertEquals(1, game.getWinner());
        }
    }

    @Test 
    public void testRunCorrectWins(){
        boolean wins;
        for (int i = 0; i < 100; i++){
            wins = false;
            Card[] pack = new Card[0];
            CardGame game = new CardGame(5);
            try {
                pack = game.readPack(5, "TestPacks/testpack5.txt");
            } catch (FileNotFoundException e){}
            game.deal(pack);
            game.runGame();

            for (int j = 1; j < 4; j++){
                if (game.getWinner() == j){
                    wins = true;
                }
            }
            assertTrue(wins);
        }
    }

    @Test 
    public void testRunP100NoWin(){
        for (int i = 0; i < 1000; i++){
            Card[] pack = new Card[0];
            CardGame game = new CardGame(5);
            try {
                pack = game.readPack(100, "TestPacks/testpack6.txt");
            } catch (FileNotFoundException e){}
            game.deal(pack);
            game.runGame();

            assertTrue(game.getWinner() != 100);
        }
    }
}
