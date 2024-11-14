import java.util.*;

public class AIPlayer extends Player{
    private Board board;

    /**
     * Default constructor for the AI player
     * @param board The board that the game is being played on
     */
    public AIPlayer(Board board)
    {
        super();

        this.board = board;
    }

    /**
     * The main function that organizes the AI's turn.
     */
    public void aiTurn()
    {
        //Find all the valid locations in the board to place a word

        //For each valid line the AI can place in
            //Check if there exists a word made up of the board's letter and the letters on the rack
            //If so, add into the AI's usedLetters and usedLocations
                //Call addWord from the board

        //If made it through and no possible words, randomly exchange 3 letters on rack
    }

    /**
     * This function determines what word the player will place, and where
     */
    private void decideWord()
    {
        //Determine all the valid locations the AI can place a word and how large

        //For each valid location/direction:
            //attempt to find a valid word that includes the valid location letter + letters on its rack
                //Ensure the validity of its placement

    }

    /**
     * This function determines whether a given starting location is valid to add a word to, and in what direction
     * @param i the row of the board to check
     * @param j the column of the board to check
     * @return An array of integers containing the number of letters that can be placed to each side of the inputted location. The directions start with north and go clockwise.
     */
    private int[] validWordLocation(int i, int j)
    {
        int[] freeSpacesInDirection = new int[4];
        Letter[][] boardAppearance = board.getBoardAppearance();
        int offsetFromInput = 1;

        //North
        if(boardAppearance[i - 1][j] == null)
        {

            while((boardAppearance[i - offsetFromInput][j]) == null && ((i - offsetFromInput) >= 0))
            {
                offsetFromInput ++;
            }

            freeSpacesInDirection[0] = offsetFromInput;
        }
        //East
        if(boardAppearance[i][j + 1] == null)
        {
            offsetFromInput = 1;

            while((boardAppearance[i][j + offsetFromInput] == null) && ((j + offsetFromInput) < Board.BOARD_SIZE))
            {
                offsetFromInput ++;
            }

            freeSpacesInDirection[1] = offsetFromInput;
        }
        //This is one row down, aka South
        if(boardAppearance[i + 1][j] == null)
        {
            offsetFromInput = 1;

            while((boardAppearance[i + offsetFromInput][j] == null) && ((i + offsetFromInput) < Board.BOARD_SIZE))
            {
                offsetFromInput ++;
            }

            freeSpacesInDirection[2] = offsetFromInput;
        }
        //West
        if(boardAppearance[i][j - 1] == null)
        {
            offsetFromInput = 1;

            while((boardAppearance[i][j - offsetFromInput] == null) && ((j - offsetFromInput) >= 0))
            {
                offsetFromInput ++;
            }

            freeSpacesInDirection[1] = offsetFromInput;
        }

        return freeSpacesInDirection;
    }

    /**
     * This method returns all the possible words that can be made from a particular location
     * @param i The row of the location to be examined
     * @param j The column of the location to be examined
     * @return An arraylist that holds all the possible words that can be placed at one location on the board, sorted by score
     */
    public ArrayList<String> possibleWords(int i, int j, int max_size)
    {
        Letter boardLetter = board.getBoardAppearance()[i][j];
        ArrayList<String> validWords = new ArrayList<>();
        int wordLength = 2;
        Random rand = new Random();

        while(wordLength < max_size)
        {
            ArrayList<Letter> unusedLetters = new ArrayList<>(rack); //Holds all the letters that the AI can still use
            unusedLetters.add(boardLetter);
            StringBuffer wordBuilt =  new StringBuffer(8); //Need to re-initiate with new every cycle

            for(int k = 0; k < wordLength; k++)
            {
                //adding a random unused letter to the end of the word
                wordBuilt.append(unusedLetters.remove(rand.nextInt(8)).getLetter());
            }

            if(Board.words.contains(wordBuilt.toString()))
            {
                validWords.add(wordBuilt.toString());
            }

            wordLength ++;
        }

        return validWords;
    }
}
