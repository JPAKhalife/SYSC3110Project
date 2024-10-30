import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * The GameController class contains the event handling for ActionEvents.
 * These events are caused whenever the user interacts with the view.
 * @author Elyssa Grant, John Khalife
 * @date 08/18/2024
 */
public class GameController implements ActionListener {
    Game game;

    /**
     * Constructor of the game controller class
     *
     * @param game - an object that tracks the current state of scrabble.
     */
    public GameController(Game game) {
        this.game = game;
    }


    //Formatting for differentiating between actions:
    //board,<y(row)>,<x(col)> - x being int and y being letter.
    //NOTE: the reason y comes first is because that's the way the data is organized in the back end.
    //menu,<keyword> - keyword being restart,quit, ect
    //rack,<index> - index being a number from 0-6
    //turn,<keyword> - keyword being submit,pass,exchange

    /**
     * This method reads the actionEvent sent when the user interacts with the view,
     * then interacts with the controller
     *
     * @param e - details about player's interaction with the GUI.
     */
    public void actionPerformed(ActionEvent e) {
        String[] command = e.getActionCommand().split(",");

        if (command[0].equals("board")) {
            //Grab the x and y coordinates
            char y = command[1].charAt(0); //This should be a single letter
            int x = Integer.valueOf(command[2]);
            //Add these to the addCoordinate method
            game.getCurrentPlayer().addCoordinate(y, x); //DNE

        } else if (command[0].equals("rack")) {
            //Grab + place the index
            int index = Integer.valueOf(command[1]);
            game.getCurrentPlayer().placeLetter(index); //DNE

        } else if (command[0].equals("turn")) {
            int winner = -1; //Holds the winning player, if any
            if (command[1].equals("submit")) {
                //Getting the combination of letters and locations
                Dictionary<ArrayList<Letter>, ArrayList<String>> wordLocation = game.getCurrentPlayer().playerTurn(1);

                int score = game.addWord(wordLocation);

                //adding the combination of letters and locations to the board
                if (score < 0) { //DNE
                    //going to need to update GUI (since we temporarily place those letters on the board when selecting)
                    game.handleBoardError();

                    //Returning early so that the user can re-try their turn instead of it being passed to the next player
                    return;
                }
                else
                {
                   boolean gameNotOver =  game.getCurrentPlayer().updateScore(score);

                   if(!gameNotOver)
                   {
                       winner = game.findWinner();
                       return; //Game is over --> don't need to move onto next turn
                   }
                }

            } else if (command[1].equals("exchange")) {
                //put exchange behavior here
                game.getCurrentPlayer().playerTurn(2); //DNE
            } else if (command[1].equals("pass")) {
                //Don't need to do anything special here
            }

            //changing to the next player's turn
            game.handleNewTurn();

        } else if (command[0].equals("menu")) {
            if (command[1].equals("restart")) {
                //Handle restarting the game. game.restart()?
            } else if (command[1].equals("quit")) {
                //Handle quitting the game? (this could be done in the view by specifying a window close behavior)
            }
            //insert checks for other menu buttons here
        }
    }


}
