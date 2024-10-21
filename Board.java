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
    public static HashSet<String> words;

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
                String word = reader.nextLine();
                words.add(word);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("There was an error reading from file.");
            e.printStackTrace();
        }
    }

    /**
     * Checks if the word being placed on the board is a valid word in the game's
     * word bank
     * 
     * @param letters The letters of the word being placed
     * @return whether the word can be placed on the board
     */
    private boolean isWord(ArrayList<Letter> letters) {
        String letterString = "";
        // convert ArrayList of Letters to a string
        for (Letter s : letters) {
            letterString += s.getLetter();
        }
        // check if string is a valid word in the enum
        if (words.contains(letterString)) {
            return true;
        }
        return false;
    }

    /**
     * Check if the placement of a particular letter is avalible on the board
     * 
     * @param location The square the letter is being placed on
     * @return whether the letter can be placed
     */
    private boolean isValidPlacement(String location) {
        // location is not a two-dimensional coordinate (number may be 2 digits)
        if (location.length() > 3) {
            return false;
        }
        // break loaction into each coordinate (ignoring case)
        // get letter from first index of string
        char letter = Character.toLowerCase(location.charAt(0));
        // get digits from second index on wards, checking that there are no letters
        String number = "";
        for (int i = 1; i < location.length(); i++) {
            // character is not a number
            if (!Character.isDigit(location.charAt(i))) {
                return false;
            }
            // character is a number
            number += Character.toString(location.charAt(i));
        }

        // diagonal value is not a letter
        if (!Character.isLetter(letter)) {
            return false;
        }

        // horizontal value is out of board bounds
        if (letter > 'o') {
            return false;
        }
        // vertical vale is out of board bounds
        if (Integer.parseInt(number) > 15) {
            return false;
        }

        // check is placement is taken already
        if (board[letter - 'a'][Integer.parseInt(number) - 1] != null) {
            return false;
        }

        return true;
    }

    /**
     * Add the word the player wants to play to the board
     * @param word The word being placed on the board, letterLocation The location
     *             on the board of each letter in the word
     * @param letterLocation The list of all corresponding letter locations to each letter.
     * @return placement successfullness
     */
    public boolean addWord(ArrayList<Letter> word, ArrayList<String> letterLocation) {
        // check that each letter has a location
        if (word.size() != letterLocation.size()) {
            return false;
        }
        // check if letters can be placed in specified locations
        for (String l : letterLocation) {
            if (!isValidPlacement(l)) {
                return false;
            }
        }

        int direction; //0 is horizontal, 1 is vertical, 2 is both (meaning someone placed 1 tile,) anything else is illegal

        //Grab the direction 
        if (letterLocation.size() == 1) {
            direction = 2;
        } else if (getCoordinateFromLocation(0, letterLocation.get(0)) == getCoordinateFromLocation(0,letterLocation.get(letterLocation.size() - 1))) {
            direction = 1;
        } else {
            direction = 0;
        }

        //Verify the direction
        if (direction != 2) {
            for (int i = 1 ; i < letterLocation.size() ; i++) {
                if (getCoordinateFromLocation(direction ^ 1,letterLocation.get(i)) != getCoordinateFromLocation(direction ^ 1,letterLocation.get(i - 1))) {
                    return false;
                }
            }
        }

        //Check the appropriate direction(s) for valid words
        boolean validWordDetected = false;
        if (direction == 2) {
            for (int i = 0 ; i < direction ; i++) {
                validWordDetected = checkWordInDirection(i, word, letterLocation);
            }
        } else {
            validWordDetected = checkWordInDirection(direction, word, letterLocation);
        }
        if (!validWordDetected) {return false;}

        // All checks have passed, add word to the board
        for (int i = 0; i < word.size(); i++) {
            // get letter coordinate from location
            String location = letterLocation.get(i);
            char locationLetter = letterLocation.get(i).charAt(0);

            // get numeric coordinate from location
            int locationNumber = getCoordinateFromLocation(0,location);

            // add letter to board
            board[locationLetter - 'a'][locationNumber] = word.get(i);
        }

        return true;
    }

    /**
     * Generate a String representation of the current status of the board including
     * all the letters that have been
     * placed on it.
     * 
     * @return the String representation of the board
     */
    public String toString(){
        String strBoard = "";
        char letter = 'A';
        strBoard += "   ";
        for (int i = 1 ; i < BOARD_SIZE + 1 ; i++) {
            strBoard += "{" + i + "}";
        }
        strBoard += "\n";
        for(int i = 0; i < BOARD_SIZE; i++){
            strBoard += letter + "  ";
            letter++;
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
     * @param axis
     * @return the numeric coordinate of the location
     */
    private int getCoordinateFromLocation(int axis, String location){
        //get numeric coordinate from location
        if (axis == 0) {
            String numberString = "";
            for (int j = 1; j < location.length(); j++) {
                numberString += location.charAt(j);
            }
            return Integer.parseInt(numberString) - 1;
        } else {
            return location.charAt(0) - 'a';
        }
        
    }

    /**
     * This method grabs the slice of the board that will be added to by the player and
     * checks if the a valid word was formed thanks to the player's addition.
     * @param direction - whether to grab a vertical or horizontal slice
     * @param word - list of letters that are being placed
     * @param letterLocation - list of locations of each corresponding letter
     * @return
     */
    private boolean checkWordInDirection(int direction, ArrayList<Letter> word, ArrayList<String> letterLocation) {
        int smallestCoord = BOARD_SIZE;
        int largestCoord = 0; //These are the coordinates that are different for smallest and largest coordinate of letter
        ArrayList<Letter> line = new ArrayList<Letter>(BOARD_SIZE); //This will be passed to the isWord function.
        for (int i = 0 ; i < BOARD_SIZE ; i++) {line.add(null);}


        //Fill up the line + get largest and smallest coordinates
        for (int i = 0 ; i < letterLocation.size() ; i++) {
            int coord = getCoordinateFromLocation(direction,letterLocation.get(i));
            if (coord < smallestCoord) {
                smallestCoord = coord;
            }
            if (coord > largestCoord) {
                largestCoord = coord;
            }
            line.set(coord,word.get(i));
        }

        //Fill up the rest of the spots
        if (direction == 0) {
            for (int i = 0 ; i < line.size() ; i++) {
                if (line.get(i) == null) {
                    line.set(i,board[i][getCoordinateFromLocation(direction, letterLocation.get(0))]);
                }
            }
        } else {
            for (int i = 0 ; i < line.size() ; i++) {
                if (line.get(i) == null) {
                    line.set(i,board[getCoordinateFromLocation(direction, letterLocation.get(0))][i]);
                }
            }
        }

        //Now try every combination of smallest-largest coords + every other to see if a word has been formed.
        boolean isValidWord = false;
        int lowBuffer = 0; //These buffers are to make sure no null values are passed to isWord
        int highBuffer = 0;
        for (int i = 0 ; i <= smallestCoord ; i++) {
            if (line.get(smallestCoord - i) != null) {lowBuffer = i;}
            for (int j = 0 ; j < (BOARD_SIZE-largestCoord) ; j++) {
                if (line.get(largestCoord + j) != null) {highBuffer = j;}
                if (isWord(new ArrayList<Letter>(line.subList(smallestCoord - lowBuffer, largestCoord + highBuffer + 1)))) {
                    i = smallestCoord;
                    j = BOARD_SIZE;
                    isValidWord = true;
                }
            }
        }
       
        return isValidWord;
        
    }

} //end class