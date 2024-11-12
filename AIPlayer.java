import java.util.Dictionary;
import java.util.Hashtable;

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
        //Get the rack

        //Get the board

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
}
