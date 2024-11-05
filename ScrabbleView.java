import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;


public class ScrabbleView extends JFrame implements GameObserver {

    JButton[][] boardButtons;
    JTextPane scorePane;
    Game game;

    public ScrabbleView(){
        super("Scrabble");
        Game game = new Game();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setLayout(new BorderLayout());


        boardButtons = new JButton[15][15]; //holds the spaces on a board as buttons (occupied spaces disabled)
        JButton[] rackButtons = new JButton[7]; //holds the letters on a rack as buttons (placed letters, before sumbitted, are disabled)
        JButton[] turnButtons = new JButton[3]; //holds the buttons used for a turn (submit, exchange, skip)
        scorePane = new JTextPane();
        JTextPane currentPlayerPane = new JTextPane();
        JPanel PlayerPanel = new JPanel();
        JPanel BoardPanel = new JPanel();

        GameController gameController = new GameController(game);
        //create board buttons

        char rowChar = 'a';
        for(int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].addActionListener(gameController);
                String buttonCoordinate = Character.toString(rowChar) + Integer.toString(j);
                boardButtons[i][j].setActionCommand(buttonCoordinate);
                BoardPanel.add(boardButtons[i][j]);
            }
            rowChar++;
        }
        this.add(BoardPanel, BorderLayout.CENTER);



        JPanel rackPanel = new JPanel(new GridLayout(1,7));
        List<Player> players = game.getPlayers();
        List<Letter> letters;



        for(int i = 0; i < 7; i++){
            for(int j = 0; j < players.size(); j++) {
                Player p = players.get(j);
                letters = p.getRack();

                rackButtons[i] = new JButton(String.valueOf(letters.get(i)));
                rackButtons[i].addActionListener(gameController);
                rackButtons[i].setActionCommand("RACK_" + i);
                rackPanel.add(rackButtons[i]);
            }
        }

        String[] commands = {"Submit", "Exchange","Skip"};
        JPanel turnPanel = new JPanel(new GridLayout(1,3));
        for(int i = 0; i<3; i++){
            turnButtons[i] = new JButton(String.valueOf(commands[i]));
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

        this.add(statusPanel, BorderLayout.EAST);
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



    @Override
    public void handleBoardUpdate(ErrorEvent e) {

        if(e.getError()!= ErrorEvent.GameError.NONE){
            JOptionPane.showMessageDialog(null, e.getError().getErrorDescription());
        }

    }

    @Override
    public void handleScoreUpdate() {

        List<Player> players = game.getPlayers();
        String scores = "";
        for(int i = 0; i < players.size(); i++){
            Player player = players.get(i);
            scores = "Player" + String.valueOf(i)+ ":"+ player.getScore() + "\n";
        }
        scorePane.setText(scores);

    }
    public static void main(String[] args){
        ScrabbleView scrabbleView = new ScrabbleView();

    }
}
