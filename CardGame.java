import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CardGame {
    public static void main(String args[]){
        // initialises variables
        CardGame game = new CardGame();

        int playerNumber = 0;
        Player[] players;
        CardDeck[] decks;

        String filename = "";

        AtomicBoolean flag = new AtomicBoolean(false);
        AtomicInteger winner = new AtomicInteger(0);

        Scanner s = new Scanner(System.in);

        // takes user input for number of players
        while (playerNumber < 2){
            System.out.print("Please enter number of players: ");
            playerNumber = s.nextInt();
            s.nextLine();
            if (playerNumber < 2){
                System.out.println("Number of players must be greater than 1");
            }
        }
        Card[] pack = new Card[playerNumber*8];

        // takes input for filename of pack
        boolean run = true;
        while (run){
            System.out.print("Please enter filename of pack to load: ");
            filename = s.nextLine();
            try {
                pack = game.readPack(playerNumber, filename); // creates an array of cards for the integers in pack
                run = false;
            } catch (FileNotFoundException e){
                System.out.println("File does not exist");
            } catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Pack is too long");
            } catch (IllegalStateException e){
                System.out.println("Pack is too short");
            }
        }
        s.close();

        // creates all players and decks in the game
        decks = new CardDeck[playerNumber];
        players = new Player[playerNumber];

        for (int i = 0; i < playerNumber; i++){
            decks[i] = new CardDeck(i+1);
        }
        for (int i = 0; i < playerNumber; i++){
            players[i] = new Player(i+1, decks[i], decks[(i+1)%playerNumber], flag, winner);
        }

        // deals cards to each player in a round robin fashion
        int nextInPack = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < playerNumber; j++){
                players[j].addCard(pack[nextInPack++]);
            }
        }
        // deals cards to each deck in a round robin fashion
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < playerNumber; j++){
                decks[j].addCard(pack[nextInPack++]);
            }
        }
    }

    public Card[] readPack(int playerNumber, String filename) 
    throws FileNotFoundException, IllegalStateException, ArrayIndexOutOfBoundsException{
        Card[] pack = new Card[8*playerNumber];
        int count = 0;
        File f = new File(filename);
        try (Scanner reader = new Scanner(f)){
            while (reader.hasNextLine()){
                pack[count++] = new Card(reader.nextInt());
            }
        } catch (FileNotFoundException e){
            throw e;
        }
        if (count != 8* playerNumber){
            throw new IllegalStateException();
        }
        return pack;
    }
}
