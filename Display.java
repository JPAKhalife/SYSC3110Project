/**
 * The Display class is responsible for displaying information about the game.
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani 
 * @date 08/10/2024
 */
public class Display {
    
    //Member variables
    Game game;

    //Constructor
    public Display(Game game) {
        this.game = game;
    }

    // TODO: This may need to be changed to a getSlot method just for future upgrades to an actual GUI
    /**
     * This method is intended to print the current state of the board.
     * @param board - the board that this method should print the state of.
     */
    public void displayBoard(Board board) {
        System.out.println("Current board: ");
        System.out.println(board.toString());
    }

    /**
     * This method is intended to reveal the score of all players
     */
    public void showScores() {
            System.out.println("Player scores:");
            for (int i = 0 ; i < this.game.getPlayers().size() ; i++) {
                System.out.println("Player " + playerNum + ": " + this.game.getPlayers().get(i).getScore());
            }
    }

    //TODO: This method may not be needed, depends (I have not added it to the UML)
    /**
     * This method shows the score of a particular player.
     * @param playerNum - the player whose score will be displayed.
     */
    public void showScore(int playerNum) {
        //This method should get the name of the player or the playe number
        System.out.println("Player " + playerNum + ", your score is: " + this.game.getPlayers.get(playerNum).getScore());
    }    
}




