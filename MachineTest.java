package enigma;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import java.util.ArrayList;


/** The suite of all JUnit tests for the Alphabet class.
 *  @author Ryan Chen
 */
public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */
    private Alphabet alph = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    private int numRotors = 6;
    private int numPawls = 4;
    private Permutation perm1 = new Permutation("(AELTPHQXRU) (BKNW) (CMOY)"
            + " (DFG) (IV) (JZ) (S)", alph);
    private Permutation perm2 = new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) "
            + "(BJ) (GR) (NT) (A) (Q)", alph);
    private Permutation perm3 = new Permutation("(ABDHPEJT) (CFLVMZO"
            + "YQIRWUKXSG) (N)", alph);
    private Permutation perm4 = new Permutation("(AEPLIYWCOXMR"
            + "FZBSTGJQNH) (DV) (KU)", alph);
    private Permutation perm5 = new Permutation("(AVOLDRWFIUQ)"
            + "(BZKSMNHYC) (EGTJPX)", alph);
    private Permutation perm6 = new Permutation("(AJQDVLEOZWIYTS) "
            + "(CGMNHFUX) (BPRK)", alph);
    private Permutation perm7 = new Permutation(
            "(ANOUPFRIMBZTLWKSVEGCJYDHXQ)", alph);
    private Permutation perm8 = new Permutation(
            "(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)", alph);
    private Permutation perm9 = new Permutation(
            "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", alph);
    private Permutation perm10 = new Permutation(
            "(AFNIRLBSQWVXGUZDKMTPCOYJHE)", alph);
    private Permutation perm11 = new Permutation(
            "(ANOUPFRIMBZTLWKSVEGCJYDHXQ)", alph);
    private Permutation perm12 = new Permutation(
            "(AE) (BN) (CK) (DQ) (FU) (GY) (HW)"
                    + " (IJ) (LO) (MP) (RX) (SZ) (TV)", alph);
    private Permutation perm13 = new Permutation("(AR) (BD) (CO)"
            + " (EJ) (FN) (GT)(HK) (IV) (LM) (PW) (QZ) (SX) (UY)", alph);
    private Permutation plugboardperm = new Permutation("(GA) (KY) "
            + "(ZC) (SR) (BM)", alph);

    private Rotor rotor1 = new Reflector("A", perm1);
    private Rotor rotor2 = new FixedRotor("B", perm2);
    private Rotor rotor3 = new MovingRotor("C",
            perm3, "ERTYUIOP");
    private Rotor rotor4 = new MovingRotor("D",
            perm4, "MAJFNCO");
    private Rotor rotor5 = new MovingRotor("E",
            perm5, "ABCDE");
    private Rotor rotor6 = new MovingRotor("F",
            perm6, "SDFGHJKLA");
    private Rotor rotor7 = new MovingRotor("G",
            perm7, "ZXCVBNMLKJ");

    private ArrayList<Rotor> rotorList = new ArrayList<Rotor>();

    @Test
    public void insertRotors() {
        rotorList.add(0, rotor1);
        rotorList.add(1, rotor2);
        rotorList.add(2, rotor3);
        rotorList.add(3, rotor4);
        rotorList.add(4, rotor5);
        rotorList.add(5, rotor6);
        rotorList.add(6, rotor7);

        Machine testMachine = new Machine(alph,
                numRotors, numPawls, rotorList);
        testMachine.setPlugboard(plugboardperm);
        String[] rotorNames = {"A", "B", "C", "D", "E", "F"};
        testMachine.insertRotors(rotorNames);
        testMachine.setRotors("SICKO");
        System.out.println(testMachine.convert(14));
        System.out.println(testMachine.convert("NICE"));
    }
}
