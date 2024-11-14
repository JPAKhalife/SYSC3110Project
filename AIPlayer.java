import java.util.*;

public class AIPlayer extends Player{
    private Board board;
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

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
        //Hashmap that stores the valid placement on the board and the valid directions
        HashMap<String, int[]> validWordPlacements = new HashMap<>();
        HashMap<String, ArrayList<String>> validWordsAtLocation = new HashMap<>();

        //Determine all the valid locations the AI can place a word and how large
        for(int i = 0; i < Board.BOARD_SIZE; i++)
        {
            for(int j = 0; j < Board.BOARD_SIZE; j++)
            {
                int[] wordSizes = validWordSize(i, j);

                //Ensuring that only locations with at least one free space are added
                for(int k = 0; k < 4; k++)
                {
                    if(wordSizes[k] > 0)
                    {
                        String location = i + "," + j;
                        validWordPlacements.put(location, wordSizes);
                        validWordsAtLocation.put(location, new ArrayList<>());
                        break;
                    }
                }
            }
        }

        //For each valid location/direction:
        for(Iterator<String> iter = validWordPlacements.keySet().iterator(); iter.hasNext();)
        {
            String location = iter.next();
            //attempt to find a valid word that includes the valid location letter + letters on its rack
            for(int i = 0; i < 4; i++)
            {
                int sizeInDirection = validWordPlacements.get(location)[i];
                if(sizeInDirection > 0)
                {
                    String[] indices = location.split(",");
                    int j = Integer.parseInt(indices[0]);
                    int k = Integer.parseInt(indices[1]);
                    validWordsAtLocation.put(location, possibleWords(j, k, Math.min(sizeInDirection, 8)));
                }
            }
        }

        //Ensure the validity of its placement



    }

    /**
     * This function determines whether a given starting location is valid to add a word to, and in what direction
     * @param i the row of the board to check
     * @param j the column of the board to check
     * @return An array of integers containing the number of letters that can be placed to each side of the inputted location. The directions start with north and go clockwise.
     */
    private int[] validWordSize(int i, int j)
    {
        int[] freeSpacesInDirection = new int[4];
        Letter[][] boardAppearance = board.getBoardAppearance();
        int offsetFromInput = 0;

        //North
        if(boardAppearance[i - 1][j] == null)
        {

            while((boardAppearance[i - offsetFromInput][j]) == null && ((i - offsetFromInput) >= 0))
            {
                offsetFromInput ++;
            }

            //We do not want accidental intersections, and thus leave one empty space alone
            freeSpacesInDirection[NORTH] = offsetFromInput - 1;
        }
        //East
        if(boardAppearance[i][j + 1] == null)
        {
            offsetFromInput = 1;

            while((boardAppearance[i][j + offsetFromInput] == null) && ((j + offsetFromInput) < Board.BOARD_SIZE))
            {
                offsetFromInput ++;
            }

            //We do not want accidental intersections, and thus leave one empty space alone
            freeSpacesInDirection[EAST] = offsetFromInput - 1;
        }
        //This is one row down, aka South
        if(boardAppearance[i + 1][j] == null)
        {
            offsetFromInput = 1;

            while((boardAppearance[i + offsetFromInput][j] == null) && ((i + offsetFromInput) < Board.BOARD_SIZE))
            {
                offsetFromInput ++;
            }

            //We do not want accidental intersections, and thus leave one empty space alone
            freeSpacesInDirection[SOUTH] = offsetFromInput - 1;
        }
        //West
        if(boardAppearance[i][j - 1] == null)
        {
            offsetFromInput = 1;

            while((boardAppearance[i][j - offsetFromInput] == null) && ((j - offsetFromInput) >= 0))
            {
                offsetFromInput ++;
            }

            //We do not want accidental intersections, and thus leave one empty space alone
            freeSpacesInDirection[WEST] = offsetFromInput - 1;
        }

        return freeSpacesInDirection;
    }

    /**
     * This method returns all the possible words that can be made from a particular location
     * @param i The row of the location to be examined
     * @param j The column of the location to be examined
     * @param max_size The maximum size the word can be to fit on the board
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

    /**
     * This method provides a simple way of translating the indices of the board into the scrabble notation that the Board class relies on
     * @param i the row of the board
     * @param j the column in the board
     * @return A string representation of official scrabble notation for the inputted location
     */
    private String translateIndexToScrabbleNotation(int i, int j)
    {
        char rowChar = (char) ('a' + i);

        return Character.toString(rowChar) + j;
    }
}
