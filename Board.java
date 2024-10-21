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
    public int addWord(ArrayList<Letter> word, ArrayList<String> letterLocation) {
        int turnScore = 0; //represents score
        int direction; //0 is horizontal, 1 is vertical.

        // check that each letter has a location
        if (word.size() != letterLocation.size()) {
            System.out.println("letter location ratio invalid");
            return 0;
        }

        // check if letters can be placed in specified locations
        for (String l : letterLocation) {
            if (!isValidPlacement(l)) {
                System.out.println("invalid placedment");
                return 0;

            }
        }

        //Grab the primary direction - all letters must be placed on one axis
        if (getCoordinateFromLocation(0, letterLocation.get(0)) == getCoordinateFromLocation(0,letterLocation.get(letterLocation.size() - 1))) {
            direction = 1;
        } else {
            direction = 0;
        }

        // Temporarily add word to the board
        setWord(word,letterLocation);

        //Ensure that direction is correct and there are no empty spaces
        if (!verifyDirection(direction,word,letterLocation)) {
            setWord(null,letterLocation); //unset letters from board
            System.out.println("direction faulty");
            return 0;
        };

        //Check primary direction for valid words
        if ((turnScore += verifyWordSlice(direction,letterLocation.get(0))) == 0) {
            setWord(null,letterLocation); //unset letters from board
            System.out.println("primary direction faulty");
            return 0; // If any direction does not have a valid score, return 0
        };

        //Check secondary direction for valid words
        int points = 0;
        for (String l : letterLocation) {
            points = verifyWordSlice(direction^1, l);
            if (points == 0) {
                setWord(null,letterLocation); //unset letters from board
                System.out.println("secondary direction faulty");
                return 0;
            }
            turnScore += points;
        }
        return turnScore;
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
     * This method returns a slice of the board in either the horizontal or vertical direction.
     * @param direction - whether to grab a vertical or horizontal slice
     * @param location - Any location within the slice for reference
     * @return an arraylist containing a slice of the board
     */
    private ArrayList<Letter> grabWordSlice(int direction, String location) {
        ArrayList<Letter> line = new ArrayList<Letter>(BOARD_SIZE); //This will be passed to the isWord function.
        //This will be the total number of points awarded to the player from all the words they created on the board
        for (int i = 0 ; i < BOARD_SIZE ; i++) {line.add(null);}
        //Fill up every spot
        int unchangingCoordinate = getCoordinateFromLocation(direction ^ 1,location);
        if (direction == 1) {
            for (int i = 0 ; i < line.size() ; i++) {
                if (line.get(i) == null) {
                    line.set(i,board[i][unchangingCoordinate]);
                }
            }
        } else {
            for (int i = 0 ; i < line.size() ; i++) {
                if (line.get(i) == null) {
                    line.set(i,board[unchangingCoordinate][i]);
                }
            }
        }
        return line;
    }

    private int verifyWordSlice(int direction, String location) {
        int points = 0;
        int smallestCoord = getCoordinateFromLocation(direction,location);
        int largestCoord = getCoordinateFromLocation(direction,location);
        ArrayList<Letter> boardSlice = grabWordSlice(direction, location);
        System.out.println("board Slice:");
        for (int i = 0 ; i < boardSlice.size() ; i++) {
            if (boardSlice.get(i) == null) {
                System.out.print("_");
            } else {
                System.out.print(boardSlice.get(i).getLetter());
            }

        }
        System.out.print("\n");

        //Now grab the smallest and largest coords of the full word formed
        for (int i = 0 ; i <= smallestCoord ; i++) {
            if (boardSlice.get(smallestCoord - i) == null) {break;}
            smallestCoord = smallestCoord - i;
        }

        for (int j = 0 ; j < (BOARD_SIZE-largestCoord) ; j++) {
            if (boardSlice.get(largestCoord + j) == null) {break;}
            largestCoord = largestCoord + j;
        }



        ArrayList<Letter> scoreWord = new ArrayList<Letter>(boardSlice.subList(smallestCoord, largestCoord + 1));
        System.out.println("score word:");
        for (int i = 0 ; i < scoreWord.size() ; i++) {
            if (scoreWord.get(i) == null) {
                System.out.print("_");
            } else {
                System.out.print(scoreWord.get(i).getLetter());
            }

        }
        System.out.print("\n");


        if (!isWord(scoreWord)) {
            System.out.println("Scoreword not a word");
            return 0;
        }
        //Tally the score of the score word
        for (int i = 0 ; i < scoreWord.size() ; i++) {
            points += scoreWord.get(i).getPoints();
        }
        return points;
    }

    private void setWord(ArrayList<Letter> word, ArrayList<String> letterLocation) {
        if (word == null) {
            for (int i = 0 ; i < letterLocation.size() ; i++) {
                board[getCoordinateFromLocation(1,letterLocation.get(i))]
                        [getCoordinateFromLocation(0,letterLocation.get(i))]
                        =  null;
            }
        } else {
            for (int i = 0 ; i < letterLocation.size() ; i++) {
                board[getCoordinateFromLocation(1,letterLocation.get(i))]
                        [getCoordinateFromLocation(0,letterLocation.get(i))]
                        =  word.get(i);
            }
        }

    }

    private boolean verifyDirection(int direction, ArrayList<Letter> word, ArrayList<String> letterLocation) {
        //Verify the direction, at the same time grab the smallest and largest coordinates
        int currentCoord;
        int unchangingCoordinate = getCoordinateFromLocation(direction ^ 1,letterLocation.get(0));
        int smallestCoord = getCoordinateFromLocation(direction,letterLocation.get(0));
        int largestCoord = getCoordinateFromLocation(direction,letterLocation.get(0)); //These are the coordinates that are different for smallest and largest coordinate of letter
        for (int i = 1 ; i < letterLocation.size() ; i++) {
            if (getCoordinateFromLocation(direction ^ 1,letterLocation.get(i)) != getCoordinateFromLocation(direction ^ 1,letterLocation.get(i - 1))) {
                return false;
            }
            currentCoord = getCoordinateFromLocation(direction,letterLocation.get(i));
            smallestCoord = Math.min(currentCoord,smallestCoord);
            largestCoord = Math.max(currentCoord,largestCoord);
        }

        //Verify that everything is in contact with each other.
        //This means that there are no null values on the board between smallest and largest coordinates.
        if (direction == 1) {
            for (int i = smallestCoord ; i <= largestCoord  ; i++) {
                if (board[i][unchangingCoordinate] == null) {
                    return false;
                }
            }
        } else {
            for (int i = smallestCoord ; i <= largestCoord  ; i++) {
                if (board[unchangingCoordinate][i] == null) {
                    return false;
                }
            }
        }
        return true;
    }

} //end class