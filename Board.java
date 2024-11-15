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
    public static HashSet<String> words; //set of all valid words
    private boolean firstTurn;
    private ErrorEvent status;

    /**
     * Constructor for Board
     */
    public Board(){
        board = new Letter [BOARD_SIZE][BOARD_SIZE];
        words = new HashSet<>();
        firstTurn = true;
        loadWords();
        status = new ErrorEvent();
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

        //Clearing the errors the board has so old errors don't cause issues
        status.setError(ErrorEvent.GameError.NONE);

        // check that each letter has a location
        if (word.size() != letterLocation.size()) {
            this.status.setError(ErrorEvent.GameError.UNEQUAL_LIST_LENGTH);
            return -1;
        }

        // check if letters can be placed in specified locations
        for (String l : letterLocation) {
            if (!isValidPlacement(l)) {
                this.status.setError(ErrorEvent.GameError.INVALID_PLACEMENT);
                return -1;

            }
        }

        //If it is the first turn of the game
        // check to make sure that there is a word being placed at the middle square
        if (firstTurn) {
            if (letterLocation.size() < 2) {
                this.status.setError(ErrorEvent.GameError.FIRST_WORD_INVALID_LENGTH);
                return -1;
            }
            boolean correctStart = false;
            for (String l : letterLocation) {
                if (l.equals("h8")) {
                    correctStart = true;
                }
            }
            if (!correctStart) {
                this.status.setError(ErrorEvent.GameError.FIRST_WORD_NOT_TOUCHING_CENTER);
                return -1;
            }
        } else {
            //If it is not the first turn, it needs to be verified
            //that the word being added intersects another word.
            if (!isConnected(letterLocation)) {
                this.status.setError(ErrorEvent.GameError.WORD_NOT_CONNECTED);
                return -1;
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
            return -1;
        };

        //Check primary direction for valid words
        if ((turnScore += verifyWordSlice(direction,letterLocation.get(0))) < 0) {
            setWord(null,letterLocation); //unset letters from board
            this.status.setError(ErrorEvent.GameError.INVALID_WORD);
            return -1; // If any direction does not have a valid score, return -1
        };

        //Check secondary direction for valid words
        int points = 0;
        for (String l : letterLocation) {
            points = verifyWordSlice(direction^1, l);
            if (points < 0) {
                setWord(null,letterLocation); //unset letters from board
                this.status.setError(ErrorEvent.GameError.INVALID_INTERSECTION);
                return -1;
            }
            turnScore += points;
        }
        firstTurn = false; //if the check passes, it is never needed again.
        return turnScore;
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
     * This method gets to values and turns it into a letter and number coordinate
     * @param row - representing the letter aspect of the coordinate
     * @param col - representing the number aspect of the coordinate
     * @return a string representing the entire coordinate encompasing row and column.
     */
    private String getLocationFromCoordinates(int row, int col) {
        return (char)('a' + row) + (String.valueOf(col+1));
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

    /**
     * This method checks a wordslice for whether or not it contains a word.
     * @param direction - the direction of the word slice
     * @param location - the location of a single letter of the word.
     * @return a point score.
     */
    private int verifyWordSlice(int direction, String location) {
        int points = 0;
        int smallestCoord = getCoordinateFromLocation(direction,location);
        int largestCoord = getCoordinateFromLocation(direction,location);
        ArrayList<Letter> boardSlice = grabWordSlice(direction, location);

        //Now grab the smallest and largest coords of the full word formed
        while (boardSlice.get(smallestCoord - 1) != null) {
            smallestCoord--;
        }

        while (boardSlice.get(largestCoord + 1) != null) {
            largestCoord++;
        }

        ArrayList<Letter> scoreWord = new ArrayList<Letter>(boardSlice.subList(smallestCoord, largestCoord + 1));
        if (!isWord(scoreWord)) {
            return -1;
        }
        //Tally the score of the score word
        //Reason for checking the size: if we are checking a single letter,
        // it is likely perpendicular with no intercepts, and should not be counted for points.
        // A single word will never be entered here, as the minimum word size must be 2 on the first turn.
        // Afterwards, if one letter is placed it must be intersecting so more than one letter will be passed here.
        if (scoreWord.size() > 1) {
            for (int i = 0; i < scoreWord.size(); i++) {
                points += scoreWord.get(i).getPoints();
            }

        }
        return points;

    }

    /**
     * This method is responsible for setting + unsetting locations on the board
     * @param word - list of letters to be added
     * @param letterLocation - list of the letters locations
     */
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

    /**
     * This method is responsible for checking that all the letters have been placed on one axis,
     * and that there are no null spaces within the word
     * @param direction - the axis that the words have been placed in
     * @param word - a list of all letters being placed
     * @param letterLocation - a list of the locations of every letter being placed
     * @return a boolean representing if this check has passed
     */
    private boolean verifyDirection(int direction, ArrayList<Letter> word, ArrayList<String> letterLocation) {
        //Verify the direction, at the same time grab the smallest and largest coordinates
        int currentCoord;
        int unchangingCoordinate = getCoordinateFromLocation(direction ^ 1,letterLocation.get(0));
        int smallestCoord = getCoordinateFromLocation(direction,letterLocation.get(0));
        int largestCoord = getCoordinateFromLocation(direction,letterLocation.get(0)); //These are the coordinates that are different for smallest and largest coordinate of letter
        for (int i = 1 ; i < letterLocation.size() ; i++) {
            if (getCoordinateFromLocation(direction ^ 1,letterLocation.get(i)) != getCoordinateFromLocation(direction ^ 1,letterLocation.get(i - 1))) {
                this.status.setError(ErrorEvent.GameError.LETTERS_NOT_STRAIGHT);
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
                    this.status.setError(ErrorEvent.GameError.LETTERS_NOT_ADJACENT);
                    return false;
                }
            }
        } else {
            for (int i = smallestCoord ; i <= largestCoord  ; i++) {
                if (board[unchangingCoordinate][i] == null) {
                    this.status.setError(ErrorEvent.GameError.LETTERS_NOT_ADJACENT);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method checks to make sure that the letters placed are connected to another word.
     * @param letterLocation - a list of the locations of all letters placed
     * @return true or false depending on whether the letters are connected.
     */
    private boolean isConnected (ArrayList<String> letterLocation) {
        HashSet<String> allLetters = new HashSet<String>(letterLocation); //change to hashset for faster access
        for (int i = 0 ; i < letterLocation.size() ; i++) {
            int row = getCoordinateFromLocation(1,letterLocation.get(i));
            int col = getCoordinateFromLocation(0,letterLocation.get(i));
            //Check all four directions
            //left
            if (col > 0) {
                if (board[row][col - 1] != null && !allLetters.contains(getLocationFromCoordinates(row,col - 1))) {
                    return true;
                }
            }
            if (col < BOARD_SIZE - 1) { // right
                if (board[row][col+1] != null && !allLetters.contains(getLocationFromCoordinates(row,col + 1))) {
                    return true;
                }
            }
            if (row > 0) { // up
                if (board[row-1][col] != null && !allLetters.contains(getLocationFromCoordinates(row-1,col))) {
                    return true;
                }
            }
            if (row < BOARD_SIZE - 1) { //down
                if (board[row+1][col] != null && !allLetters.contains(getLocationFromCoordinates(row+1,col))) {
                    return true;
                }
            }
        }

        this.status.setError(ErrorEvent.GameError.WORD_NOT_CONNECTED);
        return false;
    }

    /**
     * Returns result of the previous placement on the board.
     * @return an error event that states if there was an error with the previous action and if so what type
     */
    public ErrorEvent getStatus() {
        return status;
    }

    /**
     * Returns a copy of the board's physical appearance
     * @return A copy array representing the board's current appearance
     */
    public Letter[][] getBoardAppearance()
    {
        return board.clone();
    }

    public boolean isFirstTurn() {return firstTurn;};

} //end class
