import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class CardGame {
    private Player[] players;
    private CardDeck[] decks;
    private int playerNumber;
    private CyclicBarrier latch;
    private AtomicInteger winner;


    public CardGame(int playerNumber){
        this.playerNumber = playerNumber;
        decks = new CardDeck[playerNumber];
        players = new Player[playerNumber];
        latch = new CyclicBarrier(playerNumber + 1);
        winner = new AtomicInteger(-1);

        // creates all players and decks in the game
        for (int i = 0; i < playerNumber; i++) {
            decks[i] = new CardDeck(i + 1, playerNumber);
        }
        for (int i = 0; i < playerNumber; i++) {
            players[i] = new Player(i + 1, decks[i], decks[(i + 1) % playerNumber], winner, latch);
        }
    }
    public static void main(String args[]) {
        // initialises variables
        int tempPlayerNumber = 0;
        String filename = "";

        Scanner s = new Scanner(System.in);

        // takes user input for number of players
        while (tempPlayerNumber < 2) {
            System.out.print("Please enter number of players: ");
            try {
                tempPlayerNumber = Integer.parseInt(s.nextLine());
                if (tempPlayerNumber < 2) {
                    System.out.println("Number of players must be greater than 1");
                }
            } catch (NumberFormatException e) {
                System.out.println("Player number must be a number");
            }
        }
        CardGame game = new CardGame(tempPlayerNumber);
        Card[] pack = new Card[game.getPlayerNumber() * 8];

        // takes input for filename of pack
        boolean run = true;
        while (run) {
            System.out.print("Please enter filename of pack to load: ");
            filename = s.nextLine();
            try {
                pack = game.readPack(game.getPlayerNumber(), filename); // creates an array of cards for the integers in pack
                run = false;
            } catch (FileNotFoundException e) {
                System.out.println("File does not exist");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Pack is too long");
            } catch (IllegalStateException e) {
                System.out.println("Pack is too short");
            }
        }
        s.close();

        game.deal(pack);
        game.runGame();

        System.out.println("player " + game.getWinner() + " wins!");
    }

    public Card[] readPack(int playerNumber, String filename)
        throws FileNotFoundException, IllegalStateException, ArrayIndexOutOfBoundsException {
        Card[] pack = new Card[8 * playerNumber];
        int count = 0;
        File f = new File(filename);
        try (Scanner reader = new Scanner(f)) {
            while (reader.hasNextLine()) {
                pack[count++] = new Card(Integer.parseInt(reader.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw e;
        }
        if (count != 8 * playerNumber) {
            throw new IllegalStateException();
        }
        return pack;
    }

    public int getWinner(){
        return winner.get();
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    public void deal(Card[] pack){
        // deals cards to each player in a round robin fashion
        int nextInPack = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < playerNumber; j++) {
                players[j].addCard(pack[nextInPack++]);
            }
        }
        // deals cards to each deck in a round robin fashion
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < playerNumber; j++) {
                decks[j].addCard(pack[nextInPack++]);
            }
        }
    }

    public void runGame(){
        for (int i = 0; i < playerNumber; i++) {
            players[i].start();
        }
        try {
            latch.await();
            players[0].join();
        } catch (InterruptedException e) {} catch (BrokenBarrierException e) {}
    }

    public Player[] getPlayers(){
        return players;
    }

    public CardDeck[] getDecks(){
        return decks;
    }
}
