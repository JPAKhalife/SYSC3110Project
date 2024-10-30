import java.awt.event.ActionEvent;

/**
 * The GameController class contains the event handling for ActionEvents.
 * These events are caused whenever the user interacts with the view.
 * @author Elyssa Grant, Gillian O'Connel, John Khalife, Sandy Alzabadani
 * @date 08/18/2024
 */

public class GameController {
    Game game;

    /**
     * Constructor of the game controller class
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
     *
     *
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        String[] command = e.getActionCommand().split(",");

        if (command[0].equals("board")) {
            //Grab the x and y coordinates
            char y = command[1].charAt(0); //This should be a single letter
            int x = Integer.valueOf(command[2]);
            //Add these to the addCoordinate method
            game.getCurrentPlayer().addCoordinate(y,x); //This was written before currentplayer and addCoordinate methods existed

        } else if (command[0].equals("rack")) {
            //Grab + place the index
            int index = Integer.valueOf(command[1]);
            game.getCurrentPlayer().placeLetter(index);

        } else if (command[0].equals("turn")) {
            if (command[1].equals("submit")) {
                if (game.addWord() < 0) {
                    //going to need to update GUI
                }

            } else if (command[1].equals("exchange")) {
                //put exchange behavior here
                game.getCurrentPlayer().exchange()
            } else if (command[1].equals("pass")) {
                game.incrementTurn()
            }

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
