import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
public class GameTest {

    Game game;

    @Before
    public void setUp() throws Exception
    {
        game = new Game(4,0,"board.xml");
    }

    @After
    public void tearDown() throws Exception
    {
        LetterBag.emptyBag();
    }

    @Test
    public void getCurrentPlayer() {

        //checking to make sure the player obtained is different after turn changes
        Player player1 = game.getCurrentPlayer();
        game.handleNewTurn();
        Player player2 = game.getCurrentPlayer();
        assertNotSame(player1, player2);

        //ensuring players 3 and 4 are unique
        game.handleNewTurn();
        Player player3 = game.getCurrentPlayer();
        assertNotSame(player1, player3);
        assertNotSame(player2, player3);

        game.handleNewTurn();
        Player player4 = game.getCurrentPlayer();
        assertNotSame(player1, player4);
        assertNotSame(player2, player4);
        assertNotSame(player3, player4);

        //since 4 players, the next turn should be player 1 again
        game.handleNewTurn();
        Player player5 = game.getCurrentPlayer();
        assertSame(player1, player5);
    }

    @Test
    public void getPlayers() {
        Game game2 = new Game(4,0,"board.xml");

        //ensuring that the players list is not null
        assertNotNull(game.getPlayers());
        assertNotNull(game2.getPlayers());

        //Initial should have 4 players
        assertEquals(4, game.getPlayers().size());

        //adding different numbers of players to both games
        game.addPlayer();
        game2.addPlayer();
        //checking the sizes have changed
        assertEquals(game.getPlayers().size(), 5);
        assertEquals(game2.getPlayers().size(), 5);

        //Ensuring that the arraylist returned is a copy, not the same memory reference
        ArrayList<Player> list1 = game.getPlayers();
        ArrayList<Player> list2 = game.getPlayers();
        assertNotSame(list1, list2);
    }

    @Test
    public void getBoard() {
        assertNotNull(game.getBoard());
    }

    @Test
    public void addPlayer() {
        //Should be 4 players at start with the basic constructor
        assertEquals(4, game.getPlayers().size());
        //adding players
        game.addPlayer();
        assertEquals(5, game.getPlayers().size());
        game.addPlayer();
        assertEquals(6, game.getPlayers().size());
        game.addPlayer();
        assertEquals(7, game.getPlayers().size());

        //Ensuring the players are unique
        ArrayList<Player> arr = game.getPlayers();
        for(int i = 1; i < arr.size(); i++)
        {
            assertNotNull(arr.get(i));
            assertNotSame(arr.get(i - 1), arr.get(i));
        }
    }

    @Test
    public void findWinner() {
        //At the moment, everyone should have the same score --> No one has won yet
        assertEquals(-1, game.findWinner());

        //Artifically adding scores to Player 0 so they "win"
        game.getCurrentPlayer().updateScore(1);
        assertEquals(0, game.findWinner());

        //Adding a higher score to Player 1 so they win now
        game.handleNewTurn();
        game.getCurrentPlayer().updateScore(5);
        assertEquals(1, game.findWinner());

        //giving player 2 a higher score
        game.handleNewTurn();
        game.getCurrentPlayer().updateScore(8);
        assertEquals(2, game.findWinner());

        //giving player 3 the highest score and ensuring that it is not their turn when the score is added
        game.handleNewTurn();
        game.getCurrentPlayer().updateScore(12);
        game.handleNewTurn();
        assertEquals(3, game.findWinner());
    }

    @Test
    public void addWord() {
        //The game returns the correct number of points for a singular letter placed on the board
        ArrayList<Letter> rack = game.getCurrentPlayer().getRack();
        game.getCurrentPlayer().placeLetter(0);
        game.getCurrentPlayer().addCoordinate('a',0);
        assertEquals(-1, game.addWord(game.getCurrentPlayer().playerTurn(1)));
    }

    @Test
    public void handleNewTurn() {
        //Ensuring all the players are unique
        Player p1 = game.getCurrentPlayer();
        game.handleNewTurn();
        Player p2 = game.getCurrentPlayer();

        assertNotSame(p1, p2);
        game.handleNewTurn();
        Player p3 = game.getCurrentPlayer();

        assertNotSame(p2, p3);
        assertNotSame(p1, p3);

        game.handleNewTurn();
        Player p4 = game.getCurrentPlayer();
        assertNotSame(p1, p4);

        //Ensuring the player rolls over to the first player after the number of players has been met
        game.handleNewTurn();
        Player p5 = game.getCurrentPlayer();
        assertSame(p1, p5);
    }

    @Test
    public void handleBoardError() {
        //This is simply an updating function --> unit testing does not work easily
    }

    @Test
    public void getView()
    {
        ArrayList<GameObserver> views = game.getViews();

        assertEquals(0, views.size());

        ScrabbleView view1 = new ScrabbleView();

        game.addView(view1);
        views = game.getViews();
        assertEquals(1, views.size());
        assertSame(view1, views.get(0));

        ScrabbleView view2 = new ScrabbleView();
        game.addView(view2);
        views = game.getViews();
        assertEquals(2, views.size());
        assertSame(view2, views.get(1));

    }
}