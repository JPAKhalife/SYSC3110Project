import java.sql.Array;
import java.util.*;

public class AIPlayer extends Player{
    private Board board;
    Random rand;
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
        rand = new Random();
    }

    /**
     * The main function that organizes the AI's turn.
     */
    public void aiTurn()
    {
        //Attempt to place words on the board
        boolean placedWord = decideWord();

        //If made it through and no possible words, randomly exchange 3 letters on rack
        if(!placedWord)
        {
            for(int i = 0; i < 3; i++)
            {
                playedLetters.add(rack.get(rand.nextInt(rack.size())));
            }

            playerTurn(2);
        }
    }

    /**
     * This function determines what word the player will place, and where
     */
    private boolean decideWord()
    {
        //HashMap of information about different valid ways to add words to the board
        ArrayList<WordPlacementEvent> wordInfo = new ArrayList<>();
        if(board.isFirstTurn())
        {
            //The AI will always play a south-facing word when it starts out
            wordInfo.addAll(possibleWords("7,7", 7, SOUTH));
        }
        else {
            //Determine all the valid locations the AI can place a word and how large
            for (int i = 0; i < Board.BOARD_SIZE; i++) {
                for (int j = 0; j < Board.BOARD_SIZE; j++) {
                    int[] wordSizes = validWordSize(i, j);

                    //Ensuring that only locations with at least one free space are added
                    for (int k = 0; k < 4; k++) {
                        //If there is room on the side we are adding the word AND the word is not being appended onto another word on the other side
                        if (wordSizes[k] > 0 && wordSizes[k + 2 % 4] > 0) {
                            String location = i + "," + j;

                            //Find the potential new words and add them to the list of words
                            wordInfo.addAll(possibleWords(location, wordSizes[k], k));
                        }
                    }
                }
            }
        }

        //For all the words, attempt to add them to the board
        for(WordPlacementEvent word: wordInfo)
        {
            int result = tryAddToBoard(word);

            if(result > 0)
            {
                updateScore(result);
                return true;
            }
        }

        //Went through all the generated words and none worked
        return false;
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
     * @param location the indexes of the main location, separated by commas
     * @param max_size the maximum number of letters the word is allowed to be
     * @param direction The cardinal direction the word will be going from the original location
     * @return An arraylist that holds all the possible words that can be placed at one location on the board, sorted by score
     */
    private ArrayList<WordPlacementEvent> possibleWords(String location, int max_size, int direction)
    {
        //Obtaining the indices for the letter the word is built around
        String[] indices = location.split(",");
        int i = Integer.parseInt(indices[0]);
        int j = Integer.parseInt(indices[1]);
        char boardLetter = ' ';
        if(!board.isFirstTurn())
        {
            boardLetter = board.getBoardAppearance()[i][j].getLetter();
        }

        //Declaring variables
        ArrayList<WordPlacementEvent> validWords = new ArrayList<>();
        int wordLength = 2;

        while(wordLength < max_size) {

            //We will make an average of 7 attempts to create a valid word for each size
            for(int t = 0; t < rack.size(); t++)
            {
                ArrayList<Letter> unusedLetters = new ArrayList<>(rack); //Holds all the letters that the AI can still use
                StringBuffer wordBuilt = new StringBuffer(8); //Need to re-initiate with new every cycle
                ArrayList<Letter> addedLetters = new ArrayList<>(); //Holds all the added letters that the AI has used

                //The first letter will either be the first or last letter, so can build the rest without worrying about sub-permutations
                for (int k = 0; k < wordLength - 1; k++)
                {
                    Letter usedLetter = unusedLetters.remove(rand.nextInt(8));
                    //adding a random unused letter to the end of the word
                    wordBuilt.append(usedLetter.getLetter());
                    addedLetters.add(usedLetter);
                }

                //The board letter needs to be at the START of the word if the word goes east or south, and at the END of the word if the word goes north or west
                if(board.isFirstTurn() && Board.words.contains(wordBuilt.toString()))
                {
                    //yes, this is duplicate code, but the if statement was getting way too difficult to maintain
                    validWords.add(new WordPlacementEvent(location, direction, wordBuilt.toString(), addedLetters));
                }
                else if (!board.isFirstTurn() && (direction % 3 != 0 && Board.words.contains(boardLetter + wordBuilt.toString())) || (direction % 3 == 0 && Board.words.contains(wordBuilt.toString() + boardLetter))) {
                    //Creating a new placement
                    validWords.add(new WordPlacementEvent(location, direction, wordBuilt.toString(), addedLetters));
                }
            }
            wordLength++;
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

        //Need to add the +1 because the columns begin at 1
        return Character.toString(rowChar) + (j + 1);
    }

    /**
     * This method translates all the information into language the board understands and attempts to add the word
     * @param word The WordPlacementEvent with all the information about the word
     * @return The score from adding the word to the board
     */
    private int tryAddToBoard(WordPlacementEvent word)
    {
        //Determine the places the word starts
        String[] startingIndices = word.getLocation().split(",");
        int startRow = Integer.parseInt(startingIndices[0]);
        int startColumn = Integer.parseInt(startingIndices[1]);

        //Getting all the locations the word crosses
        for(int i = 0; i < word.wordLength(); i++)
        {
            if(word.getDirection() == SOUTH)
            {
                //Need to start 1 after the starting index, since that is already on the board
                playedLocations.add(translateIndexToScrabbleNotation(startRow + i + 1, startColumn));
            }
            else if(word.getDirection() == EAST)
            {
                playedLocations.add(translateIndexToScrabbleNotation(startRow, startColumn + i + 1));
            }
            else if(word.getDirection() == NORTH)
            {
                //Since the letters are in order, we need to start from the topmost one, hence the subtraction
                playedLocations.add(translateIndexToScrabbleNotation(Math.abs(startRow - (word.wordLength() - i)), startColumn));
            }
            else //WEST
            {
                //Since the letters are in order, their positions need to start with the leftmost one and move towards the "starting index"
                playedLocations.add(translateIndexToScrabbleNotation(startRow, Math.abs(startColumn - (word.wordLength() - i))));
            }
        }

        //add the word to the board
        return board.addWord(word.getLetters(), playedLocations);
    }
}
