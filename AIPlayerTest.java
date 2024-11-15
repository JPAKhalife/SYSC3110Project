import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class AIPlayerTest {
    private AIPlayer player;
    private Board board;
    private Game game;

    @Before
    public void setUp()
    {
        Game game = new Game(4);
        board = new Board();
        player = new AIPlayer(board);
    }

    @Test
    public void aiTurn()
    {
        ArrayList<Letter> rack =  player.getRack();
        assertNotEquals(0, rack.size());
        player.aiTurn();

        ArrayList<Letter> afterRack = player.getRack();
        assertNotSame(rack,afterRack);

        player.aiTurn();
        player.aiTurn();

        System.out.println(board);
    }
}