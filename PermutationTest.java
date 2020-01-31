package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.ArrayList;
import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Ryan Chen
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of TOALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test

    public void check() {
        Alphabet check = new Alphabet("ABCD");
        perm = new Permutation("(AB)        (D)", check);
        checkPerm("DAB", "ABCD", "BACD");
    }
    @Test
    public void checksplitCycles() {
        String cycles = "(abcde) (ghf) (z)";
        ArrayList<String> expected = new ArrayList<>();
        ArrayList<String> outcome = new ArrayList<>();
        expected.add("abcde");
        expected.add("ghf");
        expected.add("z");
        outcome = Permutation.splitCycles(cycles);
        assertArrayEquals(expected.toArray(), outcome.toArray());
    }
    @Test
    public void testInvertChar() {
        Alphabet hello = new Alphabet();
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", hello);
        assertEquals(p.invert('B'), 'A');
        assertEquals(p.invert('G'), 'G');
        assertEquals(p.invert('C'), 'J');
        assertNotEquals(p.invert('B'), 'a');
    }

    @Test
    public void testPermuteChar() {
        Alphabet hello = new Alphabet();
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", hello);
        assertEquals(p.permute('A'), 'B');
        assertEquals(p.permute('C'), 'J');
        assertNotEquals(p.permute('B'), 'a');
    }

    @Test
    public void testDerangement() {
        Alphabet hello = new Alphabet();
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW) (JC)", hello);
        assert (p.derangement());
    }
}

