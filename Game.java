import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

//** NOTE: This class doesn't actually need to exist, just copy paste the loading method to whatever class checks if a play is valid (probabbly board) */
public class Game {

    //Completely useless constructor
    Game() {
        words = new HashSet<>();
        loadWords();
    }


    HashSet<String> words;
    
    //This should be called in the constructor
    /**
     * This method loads valid words that can be used in Scrabble to memory so they can be checked against player input.
     */
    private void loadWords() {
                //This is where words will be stored.     
                try {
                    File inputFile = new File("words.txt");
                    Scanner reader = new Scanner(inputFile);
                    while (reader.hasNextLine()) {
                        words.add(reader.nextLine()); 
                    }
                    reader.close();
                } catch (Exception e) {
                    System.out.println("There was an error reading from file.");
                    e.printStackTrace();
                }
    }

    //Here is an isValid method to test
    /**
     * This method takes a word and returns whether or not it is in the hash set.
     * @param word - A string containing the word the function should search for in the hash set
     * @return boolean that says wether or not the word is valid
     */
    public boolean isValidWord(String word) {
        return words.contains(word.toLowerCase());
    }

    /**
     * This method returns all of the players in the game.
     * @return An ArrayList of players
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }
}


