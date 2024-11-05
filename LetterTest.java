import org.junit.Test;

import static org.junit.Assert.*;

public class LetterTest {

    @Test
    public void getLetter() {
        Letter letter1 = new Letter('a', 1);
        Letter letter2 = new Letter('b', 3);
        Letter letter3 = new Letter('c', 4);

        assertEquals('a', letter1.getLetter());
        assertEquals('b', letter2.getLetter());
        assertEquals('c', letter3.getLetter());

    }

    @Test
    public void getPoints() {
        Letter letter1 = new Letter('a', 1);
        Letter letter2 = new Letter('b', 3);
        Letter letter3 = new Letter('c', 4);

        assertEquals(1, letter1.getPoints());
        assertEquals(3, letter2.getPoints());
        assertEquals(4, letter3.getPoints());
    }
}