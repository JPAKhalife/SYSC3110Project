import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;


public class ScrabbleView extends JFrame implements GameObserver {

    JButton[][] boardButtons;
    JTextPane scorePane;
    Game game;
    Container turnElements;
    JButton[] rackButtons;  //holds the letters on a rack as buttons (placed letters, before sumbitted, are disabled)
    Color TILE_COLOUR = new Color(240, 215, 149);
    Color BOARD_COLOUR = new Color(103, 128, 78);
    Color BOARD_CENTER = new Color(63, 146, 199);
    JTextPane currentPlayerField;

    public ScrabbleView(){
        //Configure frame
        super("Scrabble");
        game = new Game();
        game.addView(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Create GUI elements in frame
        turnElements = new Container(); //holds the current player's rack and the turn buttons
        boardButtons = new JButton[15][15]; //holds the spaces on a board as buttons (occupied spaces disabled)
        JButton[] turnButtons = new JButton[3]; //holds the buttons used for a turn (submit, exchange, skip)
        scorePane = new JTextPane();
        JTextPane currentPlayerPane = new JTextPane();
        JPanel PlayerPanel = new JPanel();
        rackButtons = new JButton[7];

        //Create controller
        GameController gameController = new GameController(game);

        //create board buttons
        char rowChar = 'a';
        JPanel boardPanel = new JPanel(new GridLayout(Board.BOARD_SIZE,Board.BOARD_SIZE));
        for(int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].setFont(new Font(null, Font.BOLD, 14));
                boardButtons[i][j].setBackground(BOARD_COLOUR);
                boardButtons[i][j].addActionListener(gameController);
                String buttonCoordinate = "board," + Character.toString(rowChar) + "," + Integer.toString(j + 1);
                boardButtons[i][j].setActionCommand(buttonCoordinate);
                boardPanel.add(boardButtons[i][j]); //add to panel to be placed in frame
            }
            rowChar++;
        }

        boardButtons[7][7].setBackground(BOARD_CENTER);

        JPanel rackPanel = new JPanel(new GridLayout(1,7));
        Player currentPlayer= game.getCurrentPlayer();
        ArrayList<Letter> playerLetters = currentPlayer.getRack();
        for(int i = 0; i < 7; i++){
            rackButtons[i] = new JButton(Character.toString(playerLetters.get(i).getLetter()).toUpperCase());
            rackButtons[i].setBackground(TILE_COLOUR);
            rackButtons[i].setFont(new Font(null, Font.BOLD, 14));
            rackButtons[i].addActionListener(gameController);
            rackButtons[i].setActionCommand("rack," + i);
            rackPanel.add(rackButtons[i]);
        }

        String[] commands = {"submit", "exchange","skip"};
        JPanel turnPanel = new JPanel(new GridLayout(3,1));
        for(int i = 0; i<3; i++){
            turnButtons[i] = new JButton(commands[i]);
            turnButtons[i].addActionListener(gameController);
            turnButtons[i].setActionCommand("turn,"+ commands[i].toLowerCase());
            turnPanel.add(turnButtons[i]);
        }


        //Add rack and turn buttons below the board
        JPanel playPanel = new JPanel(new GridLayout(1,2));
        playPanel.add(rackPanel);
        playPanel.add(turnPanel);
        JPanel bottomDisplayPanel = new JPanel(new BorderLayout());
        currentPlayerField = new JTextPane();
        currentPlayerField.setFont(new Font(null, Font.BOLD, 12));
        currentPlayerField.setEditable(false);
        currentPlayerField.setText("Player 1 Turn");
        bottomDisplayPanel.add(currentPlayerField, BorderLayout.NORTH);
        bottomDisplayPanel.add(playPanel, BorderLayout.CENTER);

        //Create current player score pane
        scorePane.setFont(new Font(null, Font.BOLD, 14));
        scorePane.setEditable(false);
        scorePane.setText(currentScores());

        this.add(bottomDisplayPanel, BorderLayout.SOUTH);

        this.add(boardPanel, BorderLayout.CENTER);

        this.add(scorePane, BorderLayout.EAST);

        this.setVisible(true);
    }

    public void displayBoard(Letter[][] board) {
        System.out.println("Entered displayBoard\n");
        for(int i = 0; i < Board.BOARD_SIZE; i++){
            for(int j = 0; j < Board.BOARD_SIZE; j++){
                String text = (board[i][j] == null)? "": Character.toString(board[i][j].getLetter());
                System.out.println(text+ "\n");
                boardButtons[i][j].setText(text);
                boardButtons[i][j].setEnabled(text.isEmpty()); //If tile is occupied the button cannot be clicked

            }
        }
    }

    public String currentScores(){
        List<Player> players = game.getPlayers();
        String scores = "";
        scores += "Current Scores\n---------------------\n";
        for(int i = 0; i < players.size(); i++){
            Player player = players.get(i);
            scores += "Player " + String.valueOf(i + 1)+ " : "+ player.getScore() + "\n";
        }
        return scores;

    }

    @Override
    public void handleBoardUpdate(ErrorEvent e) {

        if(e.getError()!= ErrorEvent.GameError.NONE){
            JOptionPane.showMessageDialog(null, e.getError().getErrorDescription());
        }
        else
        {
            System.out.println("Going to update the board\n");
            //Update every button in the board to reflect what is in the Board class
            Letter[][] boardClone = game.getBoard().getBoardAppearance();
            displayBoard(boardClone);
        }

    }

    @Override
    public void handleScoreUpdate(int winner) {

        String scores = currentScores();

        if(winner >= 0)
        {
            scores += "Player " + winner + " won!\n";
        }

        scorePane.setText(scores);
    }

    @Override
    public void handleNewTurn(int playerNum) {
        ArrayList<Letter> newPlayerRack = game.getCurrentPlayer().getRack();

        for(int i = 0; i < 7; i++)
        {
            rackButtons[i].setText(Character.toString(newPlayerRack.get(i).getLetter()).toUpperCase());
        }

        currentPlayerField.setText("Player "+ playerNum+" Turn");
    }

    public static void main(String[] args) {
        ScrabbleView v = new ScrabbleView();
    }
}