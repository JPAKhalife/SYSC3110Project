import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrabbleView implements GameObserver {
    //declare elements of Scrabble GUI

    public ScrabbleView(){
        Game game = new Game();
        JFrame frame = new JFrame("Scrabble"); //frame for game window
        JButton[][] boardButtons = new JButton[15][15]; //holds the spaces on a board as buttons (occupied spaces disabled)
        JButton[] rackButtons = new JButton[7]; //holds the letters on a rack as buttons (placed letters, before sumbitted, are disabled)
        JButton[] turnButtons = new JButton[3]; //holds the buttons used for a turn (submit, exchange, skip)
        JTextPane scorePane = new JTextPane();
        JTextPane currentPlayerPane = new JTextPane();
        Container boardPane = new Container();
        Container turnPane = new Container();

        //create board buttons
        char rowChar = 'a';
        for(int i = 0; i < Board.BOARD_SIZE; i++){
            for(int j = 0; i <= Board.BOARD_SIZE; i++){
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].addActionListener(this);
                String buttonCoordinate = Character.toString(rowChar) + Integer.toString(j);
                boardButtons[i][j].setActionCommand(buttonCoordinate);
            }
            rowChar++;
        }

    }

    public void handleBoardUpdate(ErrorEvent e){

    }

    public void handleScore(){

    }
}
