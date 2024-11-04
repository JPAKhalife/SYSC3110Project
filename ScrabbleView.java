import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;


public class ScrabbleView extends JFrame implements GameObserver {

    JButton[][] boardButtons;
    JTextPane scorePane;
    Game game;

    public ScrabbleView(){
        Game game = new Game();
        JFrame frame = new JFrame("Scrabble"); //frame for game window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setLayout(new BorderLayout());


        boardButtons = new JButton[15][15]; //holds the spaces on a board as buttons (occupied spaces disabled)
        JButton[] rackButtons = new JButton[7]; //holds the letters on a rack as buttons (placed letters, before sumbitted, are disabled)
        JButton[] turnButtons = new JButton[3]; //holds the buttons used for a turn (submit, exchange, skip)
        scorePane = new JTextPane();
        JTextPane currentPlayerPane = new JTextPane();
        JPanel PlayerPanel = new JPanel();

        GameController gameController = new GameController(game);
        JPanel rackPanel = new JPanel(new GridLayout(1,7));
        for(int i = 0; i < 7; i++){
            rackButtons[i] = new JButton(String.valueOf(LetterBag.getNextLetter().getLetter()));
            rackButtons[i].addActionListener(gameController);
            rackButtons[i].setActionCommand("RACK_" + i);
            rackPanel.add(rackButtons[i]);

        }

        String[] commands = {"Submit", "Exchange","Skip"};
        JPanel turnPanel = new JPanel(new GridLayout(1,3));
        for(int i = 0; i<3; i++){
            turnButtons[i] = new JButton("Submit");
            turnButtons[i].addActionListener(gameController);
            turnButtons[i].setActionCommand("TURN_"+ commands[i].toUpperCase());
            turnPanel.add(turnButtons[i]);
        }



        JPanel statusPanel = new JPanel(new GridLayout(2,1));
        scorePane.setEditable(false);
        currentPlayerPane.setEditable(false);
        statusPanel.add(new JLabel("Score:"));
        statusPanel.add(scorePane);
        statusPanel.add(new JLabel("Turn:"));
        statusPanel.add(currentPlayerPane);

        this.add(statusPanel, BorderLayout.SOUTH);
        this.add(rackPanel, BorderLayout.SOUTH);
        this.add(turnPanel, BorderLayout.SOUTH);




        this.setVisible(true);
    }

    public void displayBoard(char[][] board) {
        for(int i = 0; i < Board.BOARD_SIZE; i++){
            for(int j = 0; j < Board.BOARD_SIZE; j++){
                String text = (board[i][j] == '\0')? "": String.valueOf(board[i][j]);
                boardButtons[i][j].setText(text);
                boardButtons[i][j].setEnabled(text.isEmpty()); //If tile is occupied the button cannot be clicked

            }
        }
    }

    public void showScores(){
        List<Player> players = game.getPlayers();
        String scores = "";
        for(int i = 0; i < players.size(); i++){
            Player player = players.get(i);
            scores = "Player" + String.valueOf(i)+ ":"+ player.getScore() + "\n";
        }
        scorePane.setText(scores);

    }

    @Override
    public void handleBoardUpdate(ErrorEvent e) {

    }

    @Override
    public void handleScore() {

    }
}
