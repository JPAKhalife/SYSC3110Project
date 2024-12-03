import javax.swing.*;
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
            //HANDLE TEMPORARY VIEW (letter placed on board BEFORE submitted)
            //get most recent letter placed
            Dictionary<ArrayList<Letter>, ArrayList<String>> word = game.getCurrentPlayer().playerTurn(1);
            //set the selected board tile to the letter
            for(GameObserver view: game.getViews()){
                view.handleLetterPlacement(word);
            }
        } else if (command[0].equals("rack")) {
            //Grab + place the index
            int index = Integer.valueOf(command[1]);
            game.getCurrentPlayer().placeLetter(index); //DNE
            JButton buttonPressed = (JButton) e.getSource();
            buttonPressed.setEnabled(false);

            Dictionary<ArrayList<Letter>, ArrayList<String>> word = game.getCurrentPlayer().playerTurn(1);
            //set the selected board tile to the letter
            for(GameObserver view: game.getViews()){
                view.handleLetterPlacement(word);
            }

        } else if (command[0].equals("turn")) {
            int winner = -1; //Holds the winning player, if any
            if (command[1].equals("submit")) {
                //Getting the combination of letters and locations
                Dictionary<ArrayList<Letter>, ArrayList<String>> wordLocation = game.getCurrentPlayer().playerTurn(1);
                System.out.println("before:");
                Board.printTiles();
                int score = game.addWord(wordLocation);
                System.out.println("after:");
                Board.printTiles();

                //Needed here to both clear the player's inputs and also to update score if valid
                boolean gameNotOver =  game.getCurrentPlayer().updateScore(score);

                //adding the combination of letters and locations to the board
                if (score < 0) { //DNE
                    //Returning early so that the user can re-try their turn instead of it being passed to the next player
                    game.handleBoardError();
                    return;
                }
                else
                {
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
            }else if(command[1].equals("undo")){
                //pop "move" from top of stack (getting letter and location of last move)
                int[] buttonIndices = game.getCurrentPlayer().undoPlacement();

                //if stack empty --> error event message (cannot undo)
                if(buttonIndices[0] == -1)
                {
                    for(GameObserver view: game.getViews())
                    {
                        view.handleBoardUpdate(new ErrorEvent(ErrorEvent.GameError.CANNOT_UNDO));
                    }
                }
                else{
                    //re-enable letter on rack for use
                    for(GameObserver view: game.getViews())
                    {
                        view.handleUndo(buttonIndices[0], buttonIndices[1], buttonIndices[2]);
                    }
                }

                //will need to do try/catch for future undo error (omitted until implemented)
                int[] values = game.getCurrentPlayer().undoPlacement();
                for(GameObserver view: game.getViews())
                {
                    view.handleUndo(values[0], values[1], values[2]);
                }
                game.handleNewTurn();


            }else if(command[1].equals("redo")){
                //pop "move" from top of stack (getting letter and location of last undo)
                int[] buttonIndices = game.getCurrentPlayer().redoPlacement();
                //if stack empty --> error event message (cannot redo)
                if(buttonIndices[0] == -1)
                {
                    for(GameObserver view: game.getViews())
                    {
                        view.handleBoardUpdate(new ErrorEvent(ErrorEvent.GameError.CANNOT_UNDO));
                    }
                }
                else{
                    //disable letter on rack for use
                    for(GameObserver view: game.getViews())
                    {
                        view.handleRedo(buttonIndices[0], buttonIndices[1], buttonIndices[2]);
                    }
                }

            }

            //changing to the next player's turn
            for(GameObserver view: game.getViews())
            {
                view.handleBoardUpdate(new ErrorEvent());
            }
            game.handleNewTurn();

        } else if(command[0].equals("serial")) {
            if(command[1].equals("save")){
                //pop up window for file selection
                int output = 1;
                String fileName = "";
                JFileChooser fileChooser = new JFileChooser();
                while(output != JFileChooser.APPROVE_OPTION){
                    fileChooser.setDialogTitle("Select the file to save the current game state to.");
                    output = fileChooser.showOpenDialog(null);
                }
                fileName = fileChooser.getSelectedFile().getName();

                //save game state in file


            }else if(command[1].equals("load")){
                //pop up window for file selection
                //pop up window for file selection
                int output = 1;
                String fileName = "";
                JFileChooser fileChooser = new JFileChooser();
                while(output != JFileChooser.APPROVE_OPTION){
                    fileChooser.setDialogTitle("Select the file to load game progress from.");
                    output = fileChooser.showOpenDialog(null);
                }
                fileName = fileChooser.getSelectedFile().getName();

                //load game state from file

                //update view by handler
            }

        }else if (command[0].equals("menu")) {
            if (command[1].equals("restart")) {
                //Handle restarting the game. game.restart()?
            } else if (command[1].equals("quit")) {
                //Handle quitting the game? (this could be done in the view by specifying a window close behavior)
            }
            //insert checks for other menu buttons here
        }
    }
}