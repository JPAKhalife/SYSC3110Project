import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ErrorEventTest {
    private ErrorEvent event;

    @Before
    public void setUp() throws Exception {
        event = new ErrorEvent();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getError() {
        assertEquals(ErrorEvent.GameError.NONE,event.getError());
    }

    @Test
    public void setError() {
        event.setError(ErrorEvent.GameError.INVALID_INTERSECTION);
        assertEquals(ErrorEvent.GameError.INVALID_INTERSECTION,event.getError());
        event.setError(ErrorEvent.GameError.WORD_NOT_CONNECTED);
        assertEquals(ErrorEvent.GameError.WORD_NOT_CONNECTED,event.getError());
        event.setError(ErrorEvent.GameError.LETTERS_NOT_ADJACENT);
        assertEquals(ErrorEvent.GameError.LETTERS_NOT_ADJACENT,event.getError());
        event.setError(ErrorEvent.GameError.UNEQUAL_LIST_LENGTH);
        assertEquals(ErrorEvent.GameError.UNEQUAL_LIST_LENGTH,event.getError());
    }
}
