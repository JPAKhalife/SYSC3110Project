import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;



public class ScrabbleView extends JFrame implements GameObserver, Serializable {

    private JButton[][] boardButtons;
    private JTextPane scorePane;
    private Game game;
    private JButton[] rackButtons;  //holds the letters on a rack as buttons (placed letters, before sumbitted, are disabled)
    private final Color TILE_COLOUR = new Color(240, 215, 149);
    private final Color BOARD_COLOUR = new Color(103, 128, 78);
    private final Color BOARD_CENTER = new Color(63, 146, 199);
    private final Color TRIPLE_WORD_COLOUR = new Color(227, 79, 68);
    private final Color DOUBLE_WORD_COLOUR = new Color(227, 145, 215);
    private final Color TRIPLE_LETTER_COLOUR = new Color(63, 146, 199);
    private final Color DOUBLE_LETTER_COLOUR = new Color(117, 216, 230);
    private JTextPane currentPlayerField;
    private JTextPane timerPane;
    /**
     * The basic constructor for the ScrabbleView class
     *
     * needs method to update the board after each undo/redo
     * --> called handleUndo(int locationIndexI, int locationIndexJ, int rackIndex)
     * --> called handleRedo(int locationIndexI, int locationIndexJ, int rackIndex)
     */
    public ScrabbleView(){
        //Configure frame
        super("Scrabble");
        int numPlayers = 0;
        int numAIplayers = -1;
        int totalPlayers = 0;
        while(numPlayers < 1 || numPlayers > 4)
        {
            //Get the user's desired number of players
            String playerInput = JOptionPane.showInputDialog("Please enter the number of players (1 - 4): ");

            try {
                numPlayers = Integer.parseInt(playerInput);
                totalPlayers = numPlayers;
            } catch(Exception e)
            {
                JOptionPane.showMessageDialog(this, "Please enter a valid number between 1 and 4");
            }

        }
        while(totalPlayers > 4 || numAIplayers < 0 || totalPlayers < 2) {
            String AIplayerInput = JOptionPane.showInputDialog("Please enter the number of AI players: ");

            try{
                numAIplayers = Integer.parseInt(AIplayerInput);
                totalPlayers = numAIplayers + numPlayers;
            } catch(Exception e)
            {
                JOptionPane.showMessageDialog(this, "Please enter a valid number of AIs");
            }

        }

        //Prompt user with the option to upload a custom board
        String fileName = "board.xml";
        int output = 1;
        while((output = JOptionPane.showConfirmDialog(null, "Would you like to have a custom board file?", "Specify board file", JOptionPane.YES_NO_OPTION)) == JOptionPane.CLOSED_OPTION);
        if (output == JOptionPane.YES_OPTION) {
            //Must be set to 1 and not zero, zero is approve
            JFileChooser fileChooser = new JFileChooser();
            output = 1;
            while (output != JFileChooser.APPROVE_OPTION) {
                fileChooser.setDialogTitle("Choose a file to load the premium tiles from.");
                output = fileChooser.showOpenDialog(null);
            }
            fileName = fileChooser.getSelectedFile().getName();
        }

        game = new Game(numPlayers,numAIplayers,fileName);
        game.addView(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,800);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        GameController gameController = new GameController(game);

        //Create menubar for serialization features
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game Options");
        JMenuItem saveItem = new JMenuItem("Save game as serializable");
        saveItem.setActionCommand("serial,save");
        saveItem.addActionListener(gameController);
        JMenuItem loadItem = new JMenuItem("Load game from serializable");
        loadItem.setActionCommand("serial,load");
        loadItem.addActionListener(gameController);
        JMenuItem timerMode = new JMenuItem("Timer Mode");
        timerMode.setActionCommand("timer");
        timerMode.addActionListener(gameController);
        menu.add(saveItem);
        menu.add(loadItem);
        menu.add(timerMode);
        menuBar.add(menu);

        //Create GUI elements in frame
        boardButtons = new JButton[15][15]; //holds the spaces on a board as buttons (occupied spaces disabled)
        JButton[] turnButtons = new JButton[3]; //holds the buttons used for a turn (submit, exchange, skip)
        scorePane = new JTextPane();
        timerPane = new JTextPane();
        JPanel infoPanel = new JPanel(new GridLayout(3,1));
        rackButtons = new JButton[7];

        //create board buttons
        char rowChar = 'a';
        JPanel boardPanel = new JPanel(new GridLayout(Board.BOARD_SIZE,Board.BOARD_SIZE));
        for(int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].setFont(new Font(null, Font.BOLD, 14));
                boardButtons[i][j].setBackground(getTileColor(game.getBoard().getBoardTiles()[i][j]));
                boardButtons[i][j].addActionListener(gameController);
                String buttonCoordinate = "board," + Character.toString(rowChar) + "," + Integer.toString(j + 1);
                boardButtons[i][j].setActionCommand(buttonCoordinate);
                boardPanel.add(boardButtons[i][j]); //add to panel to be placed in frame
            }
            rowChar++;
        }
        boardButtons[7][7].setBackground(BOARD_CENTER);

        Container doButtons = new Container();
        doButtons.setLayout(new GridLayout(2,1));

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

        JPanel doPanel = new JPanel(new GridLayout(1,2));
        JButton undoButton = new JButton("undo");
        undoButton.addActionListener(gameController);
        undoButton.setActionCommand("turn,undo");
        JButton redoButton = new JButton("redo");
        redoButton.addActionListener(gameController);
        redoButton.setActionCommand("turn,redo");
        doPanel.add(undoButton);
        doPanel.add(redoButton);

        String[] commands = {"submit", "exchange","skip"};
        JPanel turnPanel = new JPanel(new GridLayout(4,1));
        turnPanel.add(doPanel);
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
        timerPane.setFont(new Font(null, Font.BOLD, 14));
        timerPane.setEditable(false);
        infoPanel.add(scorePane);
        //This is an empty field to remove the grey background.
        JTextField spaceLabel =  new JTextField();
        spaceLabel.setEditable(false);
        spaceLabel.setBackground(Color.WHITE);
        spaceLabel.setBorder(BorderFactory.createEmptyBorder());
        infoPanel.add(timerPane);
        infoPanel.add(spaceLabel);





        handleScoreUpdate(-1);
        this.handleBoardUpdate(new ErrorEvent());

        this.add(bottomDisplayPanel, BorderLayout.SOUTH);

        this.add(boardPanel, BorderLayout.CENTER);

        this.add(infoPanel, BorderLayout.EAST);

        this.add(menuBar, BorderLayout.NORTH);

        this.setVisible(true);
    } //end constructor

    /**
     * Returns the colour constant associated with a tile score
     * @param tileNum
     * @return a color object
     */
    private Color getTileColor(int tileNum) {
        switch (tileNum) {
            case 2:
                return DOUBLE_WORD_COLOUR;
            case 3:
                return TRIPLE_WORD_COLOUR;
            case -2:
                return DOUBLE_LETTER_COLOUR;
            case -3:
                return TRIPLE_LETTER_COLOUR;
            default:
                return BOARD_COLOUR;
        }
    }

    /**
     * displayBoard transforms the backend board configuration into a GUI representation
     * @param board A copy of the backend board
     */
    private void displayBoard(Letter[][] board, int[][] tiles) {

        for(int i = 0; i < Board.BOARD_SIZE; i++){
            for(int j = 0; j < Board.BOARD_SIZE; j++){
                String text = (board[i][j] == null)? "": Character.toString(board[i][j].getLetter()).toUpperCase();
                boardButtons[i][j].setText(text);
                boardButtons[i][j].setEnabled(text.isEmpty()); //If tile is occupied the button cannot be clicked

                //board space colour for unoccupied spaces
                if(text.isEmpty())
                {
                    boardButtons[i][j].setBackground(getTileColor(tiles[i][j]));
                }
                else if (!text.isEmpty())
                {
                    boardButtons[i][j].setBackground(TILE_COLOUR);
                }
                //set to tile colour
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
            game.setInputWait(true);
            JOptionPane.showMessageDialog(null, e.getError().getErrorDescription());
            game.setInputWait(false);
            switch (e.getError()) {
                case CANNOT_REDO:
                    return;
                case CANNOT_UNDO:
                    return;
                default:
                    break;
            }
        }

        //Update every button in the board to reflect what is in the Board class
        Letter[][] boardClone = game.getBoard().getBoardAppearance();
        displayBoard(boardClone, game.getBoard().getBoardTiles());
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

    /**
     * Remove the tile placed by current player from the board and return it to their rack
     *
     * @param locationIndexI The row in the board where the tile was placed
     * @param locationIndexJ The coloumn in the board the tile was placed
     * @param rackIndex The index on the rack where the placed tile was
     */
    @Override
    public void handleUndo(int locationIndexI, int locationIndexJ, int rackIndex) {
        //remove placed tile from board
        boardButtons[locationIndexI][locationIndexJ].setText("");
        boardButtons[locationIndexI][locationIndexJ].setBackground(getTileColor(game.getBoard().getBoardTiles()[locationIndexI][locationIndexJ])); //TEMPORARY COLOUR
        //return to rack
        rackButtons[rackIndex].setEnabled(true);
        boardButtons[locationIndexI][locationIndexJ].setEnabled(true);
    }
    /**
     * Return the tile removed by current player undoing their turn from the board and remove it from their rack
     *
     * @param locationIndexI The row in the board where the tile was placed
     * @param locationIndexJ The coloumn in the board the tile was placed
     * @param rackIndex The index on the rack where the placed tile was
     */
    @Override
    public void handleRedo(int locationIndexI, int locationIndexJ, int rackIndex) {
        //place tile back on the board
        boardButtons[locationIndexI][locationIndexJ].setText(rackButtons[rackIndex].getText());
        boardButtons[locationIndexI][locationIndexJ].setBackground(TILE_COLOUR);
        boardButtons[locationIndexI][locationIndexJ].setEnabled(false);
        //remove tile from rack
        rackButtons[rackIndex].setEnabled(false);
    }


    @Override
    public void handleTimerUpdate(int time, boolean doTimer) {
        if (doTimer) {
            timerPane.setText("Time Remaining: " + time);
        } else {
            timerPane.setText("");
        }
    }

    /**
     * This method is used to ask the player what the stand in letter should be for a blank tile.
     */
    @Override
    public char handleBlankTile() {
        String blankLetter;
        boolean isValidLetter;
        do {
            isValidLetter = true;
            blankLetter = JOptionPane.showInputDialog("Enter the letter that the blank tile should represent:");
            if (blankLetter == null || blankLetter.length() != 1 || !Character.isLetter(blankLetter.charAt(0))) {
                isValidLetter = false;
            }
        } while (!isValidLetter);
        return blankLetter.toLowerCase().charAt(0);

    }

    public static void main(String[] args) {
        //Makes placed letter text black when board buttons are disabled
        UIManager.put("Button.disabledText", new ColorUIResource(Color.BLACK));
        //Change the default look and feel to help with macOS
        try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        } catch (Exception e) {
            e.printStackTrace();
        }
        ScrabbleView v = new ScrabbleView();
    }
}