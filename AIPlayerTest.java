import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AIPlayerTest {
    private AIPlayer player;
    private Board board;

    @Before
    public void setUp()
    {
        board = new Board();
        player = new AIPlayer(board);
    }

    @Test
    public void aiTurn()
    {
        player.aiTurn();
    }
}