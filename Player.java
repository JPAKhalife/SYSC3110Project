/**
 * This class is responsible for each player's information (their rack and score) as well as the actions they perform in a turn.
 * @author Elyssa
 * @date 2024/18/08
 */
import java.util.*;

public class Player {
    private ArrayList <Letter> rack;
    private int score;
    private ArrayList<Letter> playedLetters;
    private ArrayList<String> playedLocations;

    /**
     * Constructor for the Player class
     */
    public Player()
    {
        rack = new ArrayList<>();
        score = 0;
        playedLetters = new ArrayList<>();
        playedLocations = new ArrayList<>();

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
        int numLettersToPlay; //stores the number of letters the user wants to play
        int letterToPlay; //stores the actual letters the user plays
        Dictionary<ArrayList<Letter>, ArrayList<String>> playerWord = new Hashtable<>(); //Stores the scrabble notation for where the user wants to add things to the board
        ArrayList<Letter> letters = new ArrayList<>();
        ArrayList<String> locations = new ArrayList<>();

        if(userTurn == 1) //The user wants to place letters on the board
        {
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
        System.out.println("Letter added\n");

    }

    /**
     * addCoordinate takes in and stores a coordinate on the board in preparation for a player to submit their turn
     * @param i The row of the board
     * @param j the column of the board
     * @return whether the coordinate was successfully added
     */
    public boolean addCoordinate(char i, int j)
    {
        //ensuring that the player's location on the board is valid
        if(i >= 'a' && j >= 1 && i <= 'o' && j < 16)
        {
            String location = String.valueOf(i) + j; //combining them into a singular string representation of the location
            playedLocations.add(location); //adding the location
            System.out.println("coordinate added\n");
            return true;
        }

        return false;
    }


    /**
     * exchangeLetters takes all the letters the user indicated this round and exchanges them out with the bag
     */
    private void exchangeLetters()
    {
        for(Letter l: playedLetters)
        {
            rack.remove(l);
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
                rack.remove(l);
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
    public boolean pullFromBag()
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
     *
     * @return A copy of the player's rack
     */
    public ArrayList<Letter> getRack()
    {
        return new ArrayList<Letter>(rack);
    }
}