import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.util.Dictionary;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;


public class ScrabbleView extends JFrame implements GameObserver {
    private static final String[] tripleWordSquares = {"0,0", "0,7", "0,14", "7,0", "7,14", "14,0", "14,7", "14,14"};
    private static final String[] doubleWordSquares = {"1,1", "2,2", "3,3", "4,4", "1,13", "2,12", "3,11", "4,10", "14,1", "12,2", "12,3", "11,4", "14,14", "13,13", "12,12", "11,11", "7,7"};
    private static final String[] tripleLetterSquares = {"1,5", "1,9", "5,1", "5,5", "5,9", "5,13", "9,1", "9,5", "9,13", "13,5", "13,9"};
    private static final String[] doubleLetterSquares = {"0,3", "0,11", "2,6", "2,8", "3,7", "3,0", "3,14", "6,2", "6,6", "6,8", "6,12", "7,3", "7,11", "8,2", "8,6", "8,8", "8,12", "11,0", "11,7", "12,6", "12,8", "13,3", "13,11"};
    private JButton[][] boardButtons;
    private JTextPane scorePane;
    private Game game;
    private Container turnElements;
    private JButton[] rackButtons;  //holds the letters on a rack as buttons (placed letters, before sumbitted, are disabled)
    private final Color TRIPLE_WORD_COLOUR = new Color(227, 79, 68);
    private final Color DOUBLE_WORD_COLOUR = new Color(227, 145, 215);
    private final Color TILE_COLOUR = new Color(237, 227, 199);
    private final Color BOARD_COLOUR = new Color(214, 189, 124);
    private final Color TRIPLE_LETTER_COLOUR = new Color(63, 146, 199);
    private final Color DOUBLE_LETTER_COLOUR = new Color(117, 216, 230);
    private JTextPane currentPlayerField;

    /**
     * The basic constructor for the ScrabbleView class
     */
    public ScrabbleView(){
        //Configure frame
        super("Scrabble");
        int numPlayers = 0;
        while(numPlayers < 2 || numPlayers > 4)
        {
            //Get the user's desired number of players
            String playerInput = JOptionPane.showInputDialog("Please enter the number of players (2 - 4): ");

            numPlayers = Integer.parseInt(playerInput);
        }


        game = new Game(numPlayers);
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
                boardButtons[i][j].addActionListener(gameController);
                String buttonCoordinate = "board," + Character.toString(rowChar) + "," + Integer.toString(j + 1);
                boardButtons[i][j].setActionCommand(buttonCoordinate);
                boardPanel.add(boardButtons[i][j]); //add to panel to be placed in frame

                //check if board square is a premium square
                String boardLocation = Integer.toString(i) + "," + Integer.toString(j);
                if(isTripleWordSquare(boardLocation)) {
                    boardButtons[i][j].setBackground(TRIPLE_WORD_COLOUR);
                }else if(isDoubleWordSquare(boardLocation)){
                    boardButtons[i][j].setBackground(DOUBLE_WORD_COLOUR);
                }else if(isTripleLetterSquare(boardLocation)){
                    boardButtons[i][j].setBackground(TRIPLE_LETTER_COLOUR);
                }else if(isDoubleLetterSquare(boardLocation)){
                    boardButtons[i][j].setBackground(DOUBLE_LETTER_COLOUR);
                }else{
                   boardButtons[i][j].setBackground(BOARD_COLOUR);
                }

            }
            rowChar++;
        }

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

        handleScoreUpdate(-1);
        this.handleBoardUpdate(new ErrorEvent());

        this.add(bottomDisplayPanel, BorderLayout.SOUTH);

        this.add(boardPanel, BorderLayout.CENTER);

        this.add(scorePane, BorderLayout.EAST);

        this.setVisible(true);
    } //end constructor

    /**
     * @param location The location of the board square
     * @return whether the square is a triple word premium square
     */
    private boolean isTripleWordSquare(String location){
        for (String tripleWordSquare : tripleWordSquares) {
            if (tripleWordSquare.equals(location)) {
                return true;
            }
        }

        return false;

    }

    /**
     * @param location The location of the board square
     * @return whether the square is a double word premium square
     */
    private boolean isDoubleWordSquare(String location){
        for (String doubleWordSquare : doubleWordSquares) {
            if (doubleWordSquare.equals(location)) {
                return true;
            }
        }

        return false;

    }

    /**
     * @param location The location of the board square
     * @return whether the square is a triple letter premium square
     */
    private boolean isTripleLetterSquare(String location){
        for (String tripleLetterSquare : tripleLetterSquares) {
            if (tripleLetterSquare.equals(location)) {
                return true;
            }
        }

        return false;

    }

    /**
     * @param location The location of the board square
     * @return whether the square is a triple letter premium square
     */
    private boolean isDoubleLetterSquare(String location){
        for (String doubleLetterSquare : doubleLetterSquares) {
            if (doubleLetterSquare.equals(location)) {
                return true;
            }
        }

        return false;

    }

    /**
     * displayBoard transforms the backend board configuration into a GUI representation
     * @param board A copy of the backend board
     */
    private void displayBoard(Letter[][] board) {
        System.out.println("Entered displayBoard\n");
        for(int i = 0; i < Board.BOARD_SIZE; i++){
            for(int j = 0; j < Board.BOARD_SIZE; j++){
                String text = (board[i][j] == null)? "": Character.toString(board[i][j].getLetter()).toUpperCase();
                System.out.println(text+ "\n");
                boardButtons[i][j].setText(text);
                boardButtons[i][j].setEnabled(text.isEmpty()); //If tile is occupied the button cannot be clicked

                if(text.isEmpty())
                {
                    String boardLocation = Integer.toString(i) + "," + Integer.toString(j);
                    if(isTripleWordSquare(boardLocation)) {
                        boardButtons[i][j].setBackground(TRIPLE_WORD_COLOUR);
                    }else if(isDoubleWordSquare(boardLocation)){
                        boardButtons[i][j].setBackground(DOUBLE_WORD_COLOUR);
                    }else if(isTripleLetterSquare(boardLocation)){
                        boardButtons[i][j].setBackground(TRIPLE_LETTER_COLOUR);
                    }else if(isDoubleLetterSquare(boardLocation)){
                        boardButtons[i][j].setBackground(DOUBLE_LETTER_COLOUR);
                    }else{
                        boardButtons[i][j].setBackground(BOARD_COLOUR);
                    }


                }
                else
                {
                    boardButtons[i][j].setBackground(TILE_COLOUR);
                }


            }
        }
    }
    /**
     * currentScores builds the formatting for the scores of all players to be displayed
     * @return A string representation of the player's current scores
     */
    private String currentScores(){
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
    public void handleLetterPlacement(Dictionary<ArrayList<Letter>, ArrayList<String>> word){
        ArrayList<Letter> letters = word.keys().nextElement();
        ArrayList<String> locations = word.elements().nextElement();
        ArrayList<Letter> rack = game.getCurrentPlayer().getRack();

        try {
            int i = 0;
            for (String location : locations) {
                char y = location.charAt(0);
                int x = Integer.parseInt(location.substring(1));
                char letter = letters.get(i).getLetter();
                System.out.println("handleLetterPlacement - y: " + y + ", x: " + x + ", letter: " + letter);
                y = Character.toLowerCase(y); //make sure lower case
                //JButton placement = this.boardButtons[y - 'a'][x];
                boardButtons[y - 'a'][x - 1].setBackground(TILE_COLOUR); //Board is indexed starting with 1 --> need to go down one
                boardButtons[y - 'a'][x - 1].setFont(new Font(null, Font.BOLD, 12));
                boardButtons[y - 'a'][x - 1].setForeground(Color.BLACK);
                boardButtons[y - 'a'][x - 1].setText(Character.toString(letter).toUpperCase());
                boardButtons[y - 'a'][x - 1].setEnabled(false);

                i++;
            }
        }
        catch(IndexOutOfBoundsException e)
        {
            //Ensuring that the user clicking in the wrong order doesn't cause errors
            return;
        }
        catch(Exception exception)
        {
            return;
        }

    }

    /**
     * handleBoardUpdate handles how the GUI board should respond to all attempts at updating the board
     * @param e the error thrown by the board. If none, use the NONE error
     */
    @Override
    public void handleBoardUpdate(ErrorEvent e) {
        if(e.getError()!= ErrorEvent.GameError.NONE){
            JOptionPane.showMessageDialog(null, e.getError().getErrorDescription());
        }

        System.out.println("Going to update the board\n");
        //Update every button in the board to reflect what is in the Board class
        Letter[][] boardClone = game.getBoard().getBoardAppearance();
        displayBoard(boardClone);

        for(int i = 0; i < 7; i++)
        {
            rackButtons[i].setEnabled(true);
        }
    }

    /**
     * handleScoreUpdate handles how the GUI responds to changes in the player's scores
     * @param winner The player's integer value. If no player won, use -1
     */
    @Override
    public void handleScoreUpdate(int winner) {

        String scores = currentScores();

        if(winner >= 0)
        {
            scores += "Player " + winner + " won!\n";
        }

        scorePane.setText(scores);
    }

    /**
     * handleNewTurn handles the GUI's response to it being a new plauer's turn to place letters
     * @param playerNum The player whose turn it now is
     */
    @Override
    public void handleNewTurn(int playerNum) {
        ArrayList<Letter> newPlayerRack = game.getCurrentPlayer().getRack();

        for(int i = 0; i < 7; i++)
        {
            rackButtons[i].setText(Character.toString(newPlayerRack.get(i).getLetter()).toUpperCase());
            rackButtons[i].setEnabled(true);
        }

        currentPlayerField.setText("Player "+ (playerNum + 1) +" Turn");
    }

    public static void main(String[] args) {
        //Makes placed letter text black when board buttons are disabled
        UIManager.put("Button.disabledText", new ColorUIResource(Color.BLACK));
        ScrabbleView v = new ScrabbleView();
    }
}