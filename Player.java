/**
 * This class is responsible for each player's information (their rack and score) as well as the actions they perform in a turn.
 * @author Elyssa
 * @date 2024/18/08
 */
import java.io.Serializable;
import java.util.*;

import static java.lang.Math.min;

public class Player implements Serializable {
    protected ArrayList <Letter> rack;
    private int score;
    protected ArrayList<Letter> playedLetters;
    protected ArrayList<String> playedLocations;
    //These stacks contain data formatted as "playedLettersIndex,playedLocationsValue,rackIndex"
    private Stack<String> undoStack;
    private Stack<String> redoStack;
    private int lastLetterIndex;

    /**
     * Constructor for the Player class
     */
    public Player()
    {
        rack = new ArrayList<>();
        score = 0;
        playedLetters = new ArrayList<>();
        playedLocations = new ArrayList<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();

        this.pullFromBag(); //The first thing a player does when they enter a game is fill their rack
    }

    /**
     * Getter for the score value so that Game can determine the winner
     * @return the player's score
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * Plays one turn of scrabble using the player's rack
     * @param userTurn Holds what the user wants to do with their turn. 1 = play a word, while
     * @return A Dictionary with the word of letters as the key, and the desired locations as the value
     */
    public Dictionary<ArrayList<Letter>, ArrayList<String>> playerTurn(int userTurn)
    {
        Dictionary<ArrayList<Letter>, ArrayList<String>> playerWord = new Hashtable<>(); //Stores the scrabble notation for where the user wants to add things to the board

        if(userTurn == 1) //The user wants to place letters on the board
        {
            //It is necessary to sort in case there are any blank tiles.
            for(int i = 0; i < min(this.playedLocations.size() - 1, this.playedLetters.size() - 1); ++i) {
                int smallestIndex = i;

                for(int j = i; j < this.playedLocations.size(); ++j) {
                    if (((String)this.playedLocations.get(smallestIndex)).charAt(0) > ((String)this.playedLocations.get(i)).charAt(0) || ((String)this.playedLocations.get(smallestIndex)).charAt(1) > ((String)this.playedLocations.get(i)).charAt(1)) {
                        smallestIndex = j;
                    }
                }

                this.playedLocations.add(i, (String)this.playedLocations.get(smallestIndex));
                this.playedLocations.remove(smallestIndex + 1);
                this.playedLetters.add(i, (Letter)this.playedLetters.get(smallestIndex));
                this.playedLetters.remove(smallestIndex + 1);
            }
            playerWord.put(playedLetters, playedLocations);
        }
        else if(userTurn == 2) { //The user wants to exchange letters with the letter bag
            exchangeLetters();
        }

        return playerWord;
    }

    /**
     * placeLetter takes in and stores a letter in preparation for the player to submit their turn
     * @param rackIndex the index of the letter on the player's rack
     *
     */
    public void placeLetter(int rackIndex)
    {
        playedLetters.add(rack.get(rackIndex));
        lastLetterIndex = rackIndex;
        //Adding the new letter as an undo value
        if(playedLetters.size() <= playedLocations.size())
        {
            String undo = (playedLetters.size() - 1) + "," + playedLocations.get(playedLetters.size() - 1) + ","+ rackIndex;
            undoStack.push(undo);

            //Clearing the redo stack, since the player has changed the order of their actions
            redoStack.clear();
        }
    }


    /**
     * addCoordinate takes in and stores a coordinate on the board in preparation for a player to submit their turn
     * @param i The row of the board
     * @param j the column of the board
     * @return whether the coordinate was successfully added
     */
    public boolean addCoordinate(char i, int j)
    {
        String location = "";
        //ensuring that the player's location on the board is valid
        if(i >= 'a' && j >= 1 && i <= 'o' && j < 16)
        {
            location = String.valueOf(i) + j; //combining them into a singular string representation of the location
            playedLocations.add(location); //adding the location
            if(playedLocations.size() <= playedLetters.size())
            {
                //Getting the letter associated with the just added coordinate
                int rackIndex = lastLetterIndex;
                String undo = (playedLocations.size() - 1)+ "," + location + ","+rackIndex;
                undoStack.push(undo);
            }

            //Clearing the redo stack, since the player has changed their order of actions
            redoStack.clear();
            return true;
        }

        return false;
    }


    /**
     * exchangeLetters takes all the letters the user indicated this round and exchanges them out with the bag
     */
    protected void exchangeLetters()
    {
        for(Letter l: playedLetters)
        {
                rack.remove(l);
                LetterBag.addLetter(l);
        }

        pullFromBag();

        //Now that the user has played all their letters, they need to clear them
        playedLetters.clear();
        playedLocations.clear();
    }

    /**
     * Updates the player's score after they have played a round of scrabble, and officially removes the letters from the player's rack
     * @return A boolean describing whether the game should continue onto the next player
     */
    public boolean updateScore(int turnScore)
    {
        boolean gameNotOver = true;
        if(turnScore > 0)
        {
            score+= turnScore;

            for(Letter l: playedLetters)
            {
                if (l.getPoints() == 0) { //check for blank tile
                    rack.remove(new Letter('_',0));
                } else {
                    rack.remove(l);
                }
            }

            //AT THE MOMENT, the player pulls from the bag in main. Need to send that somewhere else while also having a way to indicate
            //that if the bag is empty, we should finish the game --> here?
            gameNotOver = pullFromBag();
        }

        //Now that the user has played all their letters, they need to clear them
        playedLetters.clear();
        playedLocations.clear();

        return gameNotOver && !isRackEmpty(); //want a false to say game is over --> if rack is empty, must return false for this to happen

    }

    /**
     * Pulls more letters from the bag to fill their rack up to 7 letters
     * @return a boolean indicating whether the rack was filled back up to 7 letters successfully
     */
    protected boolean pullFromBag()
    {
        while(rack.size() < 7)
        {
            Letter letter = LetterBag.getNextLetter();
            if(letter != null)
            {
                rack.add(letter);
            }
            else
            {
                return false;
            }
        }

        return true;
    }

    /**
     * This method returns true if the rack is empty
     * @return a boolean stating whether the rack is empty
     */
    public boolean isRackEmpty() {

        return rack.size() <= 0;
    }

    /**
     * getRack obtains a copy of the player's rack
     * @return A copy of the player's rack
     */
    public ArrayList<Letter> getRack()
    {
        return new ArrayList<Letter>(rack);
    }

    /**
     * undoPlacement undoes a previously performed move
     * @return an array of the board indices followed by the rack index
     */
    public int[] undoPlacement()
    {
        try {
            String undo = undoStack.pop();
            redoStack.push(undo); //Saving for later redo
            String[] indices = undo.split(",");

            playedLetters.remove(Integer.parseInt(indices[0]));
            playedLocations.remove(indices[1]);

            return transformToIndices(indices);
        }
        catch(EmptyStackException | IndexOutOfBoundsException e)
        {
            return new int[]{-1, -1, -1};
        }
    }

    /**
     * redoPlacement redoes a previously undone move
     * @return the board indices followed by the rack index in array form
     */
    public int[] redoPlacement()
    {
        try{
            String redo = redoStack.pop();
            undoStack.push(redo);
            String[] indices = redo.split(",");

            Letter letterToPlay = rack.get(Integer.parseInt(indices[2]));
            playedLetters.add(letterToPlay);
            playedLocations.add(indices[1]);

            return transformToIndices(indices);
        } catch(EmptyStackException e)
        {
            return new int[]{-1, -1, -1};
        }
    }

    /**
     * This function helps separate the values of the locations that are changed by an undo/redo before sending them
     * to be processed in the Controller and View
     * @param values An array of Strings where the first value is the playedLetters index, the second is a String representation
     *               of the index on the board, and the third is the index of letter on the rack
     * @return An array of integers, where the first two are the indices for the location on the board, and the final one
     *          is the index of the letter on the rack
     */
    private int[] transformToIndices(String[] values)
    {
        int[] indices = new int[3];
        //position 3 = rack index
        indices[2] = Integer.parseInt(values[2]);
        //position 2 is the column on the board
        indices[1] = Integer.parseInt(values[1].substring(1)) - 1;

        //Transforming the row into an integer value
        char row = values[1].charAt(0);
        indices[0] = row - 'a';

        return indices;
    }

    /**
     * Removes the values from the undo and redo stack to perform a new turn
     */
    public void clearCollections()
    {
        undoStack.clear();
        redoStack.clear();
        playedLetters.clear();
        playedLocations.clear();

    }
}