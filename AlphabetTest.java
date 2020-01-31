package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Alphabet class.
 *  @author Ryan Chen
 */
public class AlphabetTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Alphabet _alphabet;
    private String alpha = UPPER_STRING;

    /* ***** TESTS ***** */
    @Test
    public void checkGet() {
        Alphabet check = new Alphabet("aBcDeFg");
        assertEquals(check.get(3), 'D');
        assertEquals(check.get(4), 'e');
    }

    @Test
    public void checkToInt() {
        Alphabet check = new Alphabet("aBcDeFg");
        assertEquals(check.toInt('D'), 3);
        assertEquals(check.toInt('e'), 4);
    }

    @Test
    public void checkContains() {
        Alphabet check = new Alphabet("aBcDeFg");
        assert (check.contains('B'));
        assert (!(check.contains('A')));
    }
}
