import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AIPlayerTest {
    private AIPlayer player;
    @Before
    public void setUp()
    {
        Board board = new Board();
        player = new AIPlayer(board);
    }

    @Test
    public void aiTurn()
    {

    }
}