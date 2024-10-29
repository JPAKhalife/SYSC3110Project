import javax.swing.*;
import java.awt.*;

public class ScrabbleView {
    public ScrabbleView(){
        Game game = new Game();
        JFrame frame = new JFrame("Scrabble"); //frame for game window
        JButton[][] boardButtons = new JButton[15][15]; //holds the spaces on a board as buttons (occupied spaces disabled)
        JButton[] rackButtons = new JButton[7]; //holds the letters on a rack as buttons (placed letters, before sumbitted, are disabled)
        JButton[] turnButtons = new JButton[3]; //holds the buttons used for a turn (submit, exchange, skip)
        JTextPane scorePane = new JTextPane();
        JTextPane currentPlayerPane = new JTextPane();

    }
}
