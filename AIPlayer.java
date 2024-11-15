import java.sql.Array;
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
        //Attempt to place words on the board

        //If made it through and no possible words, randomly exchange 3 letters on rack
    }

    /**
     * This function determines what word the player will place, and where
     */
    private void decideWord()
    {
        //HashMap of information about different valid ways to add words to the board
        ArrayList<WordPlacementEvent> wordInfo = new ArrayList<>();


        //Determine all the valid locations the AI can place a word and how large
        for(int i = 0; i < Board.BOARD_SIZE; i++)
        {
            for(int j = 0; j < Board.BOARD_SIZE; j++)
            {
                int[] wordSizes = validWordSize(i, j);

                //Ensuring that only locations with at least one free space are added
                for(int k = 0; k < 4; k++)
                {
                    //If there is room on the side we are adding the word AND the word is not being appended onto another word on the other side
                    if(wordSizes[k] > 0 && wordSizes[k + 2 % 4] > 0)
                    {
                        String location = i + "," + j;

                        //Find the potential new words and add them to the list of words
                        wordInfo.addAll(possibleWords(location, wordSizes[k], k));
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
                break;
            }
        }

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
    public ArrayList<WordPlacementEvent> possibleWords(String location, int max_size, int direction)
    {
        String[] indices = location.split(",");
        Letter boardLetter = board.getBoardAppearance()[Integer.parseInt(indices[0])][Integer.parseInt(indices[1])];
        ArrayList<WordPlacementEvent> validWords = new ArrayList<>();
        int wordLength = 2;
        Random rand = new Random();

        while(wordLength < max_size)
        {
            ArrayList<Letter> unusedLetters = new ArrayList<>(rack); //Holds all the letters that the AI can still use
            StringBuffer wordBuilt =  new StringBuffer(8); //Need to re-initiate with new every cycle
            ArrayList<Letter> addedLetters = new ArrayList<>(); //Holds all the added letters that the AI has used

            //if the direction is east or south, the first letter will be the one from the board
            if(direction % 3 != 0)
            {
                wordBuilt.append(boardLetter.getLetter());
                addedLetters.add(boardLetter);
            }

            //The first letter will either be the first or last letter

            for(int k = 0; k < wordLength - 1; k++)
            {
                Letter usedLetter = unusedLetters.remove(rand.nextInt(8));
                //adding a random unused letter to the end of the word
                wordBuilt.append(usedLetter.getLetter());
                addedLetters.add(usedLetter);
            }

            //If the direction is north or west, the last letter must be the letter on the board
            if(direction % 3 == 0)
            {
                wordBuilt.append(boardLetter.getLetter());
                addedLetters.add(boardLetter);
            }

            if(Board.words.contains(wordBuilt.toString()))
            {
                //Creating a new placement
                validWords.add(new WordPlacementEvent(location, direction, wordBuilt.toString(), addedLetters));
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

    /**
     * This method translates all the information into language the board understands and attempts to add the word
     * @param word The WordPlacementEvent with all the information about the word
     * @return The score from adding the word to the board
     */
    /**
    private int tryAddToBoard(WordPlacementEvent word)
    {
        //Determine the places the word starts
        String[] startingIndices = word.getLocation().split(",");
        int startRow = Integer.parseInt(startingIndices[0]);
        int startColumn = Integer.parseInt(startingIndices[1]);

        //Getting all the locations the word crosses
        for(int i = 0; i < word.wordLength(); i++)
        {

        }

        //Translate each index location into scrabble notation and add to the board

        //add the word to the board
        return board.addWord(word.getLetters(), )
    }
     */
}
