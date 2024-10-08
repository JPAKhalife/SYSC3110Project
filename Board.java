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
        if(letterString.equalsIgnoreCase(Words)){
            return true;
        }
        return false;
    }

    private boolean isValidPlacement(ArrayList<String> location){
        //location did not give two-dimensional coordinates
        if(location.size() > 2){
            return false;
        }
        //location is not in proper [letter, number] format
        //diagonal value is not a number
        try{
            Integer.parseInt(location.get(1));
        }
        catch(NumberFormatException e){
            return false;
        }
        //diagonal value is not a letter
        char c = location.get(0).charAt(0);
        if(!Character.isLetter(c)){
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

} //end class
