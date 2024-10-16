
/**
 * This class is reposnible for checking if the word a player is looking to place on the board is valid and
 * storing the current board status (i.e. words previously placed by players).
 * @author Gillian O'Connell
 * @date 2024/10/08
 */

import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.util.*;
import java.lang.*;

public class Board {
    public static final int BOARD_SIZE = 15;
    private Letter[][] board;
    private static HashSet<String> words;

    /**
     * Constructor for Board
     */
    public Board(){
       board = new Letter [BOARD_SIZE][BOARD_SIZE];
       words = new HashSet<>();
       loadWords();
    }

    /**
     * Create the bank of vailid words that can be played in Scrabble. All words are fetched
     * from external txt file accessed from https://www.mit.edu/~ecprice/wordlist.10000
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

    /**
     * Checks if the word being placed on the board is a valid word in the game's word bank
     * @param letters The letters of the word being placed
     * @return whether the word can be placed on the board
     */
    private boolean isWord(ArrayList<Letter> letters){
        String letterString = "";
        //convert ArrayList of Letters to a string
        for(Letter s : letters){
            letterString += s.getLetter();
        }
        //check if string is a valid word in the enum
         if(words.contains(letterString)){
             return true;
         }

        return false;
    }

    /**
     * Check if the placement of a particular letter is avalible on the board
     * @param location The square the letter is being placed on
     * @return whether the letter can be placed
     */
    private boolean isValidPlacement(String location){
        //location is not a two-dimensional coordinate (number may be 2 digits)
        if(location.length() > 3){
            return false;
        }
        //break loaction into each coordinate (ignoring case)
        //get letter from first index of string
        char letter = Character.toLowerCase(location.charAt(0));
        //get digits from second index on wards, checking that there are no letters
        String number = "";
        for(int i = 1; i < location.length(); i++){
            //character is not a number
            if(!Character.isDigit(location.charAt(i))){
                return false;
            }
            //character is a number
            number += Character.toString(location.charAt(i));
        }

        //diagonal value is not a letter
        if(!Character.isLetter(letter)){
            return false;
        }

        //horizontal value is out of board bounds
        if(letter > 'o'){
            return false;
        }
        //vertical vale is out of board bounds
        if(Integer.parseInt(number) > 15){
            return false;
        }

        //check is placement is taken already
        if(board[letter - 'a'][Integer.parseInt(number)] != null){
            return false;
        }

        return true;
    }

    /**
     * Add the word the player wants to play to the board
     * @param word The word being placed on the board, letterLocation The location on the board of each letter in the word
     * @return placement successfullness
     */
    public boolean addWord(ArrayList<Letter> word, ArrayList<String> letterLocation) {
        //check that each letter has a location
        if (word.size() != letterLocation.size()) {
            return false;
        }
        //check if letters can be placed in specified locations
        for (String l : letterLocation) {
            if (!isValidPlacement(l)) {
                return false;
            }
        }
        //check if the word is a valid word
        if (!isWord(word)) {
            return false;
        }
        //add word to the board
        for (int i = 0; i < word.size(); i++) {
            //get letter coordinate from location
            String location = letterLocation.get(i);
            char locationLetter = letterLocation.get(i).charAt(0);

            //get numeric coordinate from location
            int locationNumber = getNumericCoordinate(location);

            //add letter to board
            board[locationLetter - 'a'][locationNumber] = word.get(i);
        }

        return true;
    }

    /**
     * Generate a String representation of the current status of the board including all the letters that have been
     * placed on it.
     * @return the String representation of the board
     */
    public String toString(){
        String strBoard = "";
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                if(this.board[i][j] == null){
                    strBoard += "[ ]";
                }else{
                    strBoard += "[" + this.board[i][j].getLetter() + "]";
                }
            }
            strBoard += "\n";
        }

        return strBoard;
    }

    /**
     * Take the string representation of a board location and return the numeric coordinate
     * @param location The loaction on the board
     * @return the numeric coordinate of the location
     */
    private int getNumericCoordinate(String location){
        //get numeric coordinate from location
        String numberString = "";
        for (int j = 1; j < location.length(); j++) {
            numberString += location.charAt(j);
        }
        return Integer.parseInt(numberString);
    }
} //end class
