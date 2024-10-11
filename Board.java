import java.util.*;
import java.lang.*;

public class Board {
    public static final int BOARD_SIZE = 15;
    private Letter[][] board;

    public Board(){
       board = new Letter [BOARD_SIZE][BOARD_SIZE];
    }

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

        return true;
    }

    public boolean addWord(Dictionary<Letter, ArrayList<String>> word){
        //check if word is word
        return true;
    }

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

} //end class
