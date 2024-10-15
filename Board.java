
/**
 * This class is reposnible for checking if the word a player is looking to place on the board is valid and
 * storing the game status (i.e. words previously placed by players).
 * @author Gillian O'Connell
 * @date 2024/10/08
 */

import java.io.File;
import java.util.*;
import java.lang.*;

public class Board {
    public static final int BOARD_SIZE = 15;
    private Letter[][] board;
    HashSet<String> words;

    /**
     * Constructor for Board
     */
    public Board(){
       board = new Letter [BOARD_SIZE][BOARD_SIZE];
       loadWords();
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
        /*
        ENUM NOT PRESENTLY CREATED
        THIS IS COMMENTED OUT TO AVOID ERROR
         if(letterString.equalsIgnoreCase(Words)){
            return true;
        }
         */

        return false;
    }

    /**
     * Check if the placement of a particular letter is avalible on the board
     * @param location The square the letter is being placed on
     * @return whether the letter can be placed
     */
    private boolean isValidPlacement(ArrayList<String> location){
        //location did not give two-dimensional coordinates
        if(location.size() > 2){
            return false;
        }
        //break loaction into each coordinate
        char letter = location.get(0).charAt(0);
        char digit = location.get(1).charAt(0);

        //diagonal value is not a letter
        if(!Character.isLetter(letter)){
            return false;
        }
        //diagonal value is not a digit
        if(!Character.isDigit(digit)){
            return false;
        }
        //horizontal value is out of board bounds
        if(location.get(0).toLowerCase().compareTo("o") > 0){
            return false;
        }
        //vertical vale is out of board bounds
        if(Integer.parseInt(location.get(1)) > 15){
            return false;
        }

        //check is placement is taken already
        if(board[letter][digit] != null){
            return false;
        }

        return true;
    }

    /**
     * Add the word the player wants to play to the board
     * @param word The word being placed on the board
     * @return placement successfullness
     */
    public boolean addWord(Dictionary<Letter, ArrayList<String>> word){
        //check if word is word
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
} //end class
