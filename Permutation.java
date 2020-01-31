package enigma;

import static enigma.EnigmaException.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ryan Chen
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        for (int i = 0; i < _alphabet.size(); i++) {
            charMap.put(Character.valueOf(_alphabet.get(i)),
                    Character.valueOf(_alphabet.get(i)));
            reverseCharMap.put(Character.valueOf(_alphabet.get(i)),
                    Character.valueOf(_alphabet.get(i)));
        }
        cycleList = splitCycles(cycles);
        for (int i = 0; i < cycleList.size(); i++) {
            this.addCycle(cycleList.get(i));
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm.
     *  @param cycle Cycle to be added to the permutation*/
    private void addCycle(String cycle) {
        if (cycle.length() == 1) {
            return;
        } else {
            for (int i = 0; i < cycle.length() - 1; i++) {
                charMap.replace((cycle.charAt(i)),
                        (cycle.charAt(i + 1)));
            }
            charMap.replace((cycle.charAt(cycle.length() - 1)),
                    (cycle.charAt(0)));
            for (int i = cycle.length() - 1; i > 0; i--) {
                reverseCharMap.replace((cycle.charAt(i)),
                        (cycle.charAt(i - 1)));
            }
            reverseCharMap.replace((cycle.charAt(0)),
                    (cycle.charAt(cycle.length() - 1)));
        }
    }

    /** Returns an ArrayList of cycles.
     * @param cycles The string of cycles that need
     *               to be split into individual cycles*/
    public static ArrayList<String> splitCycles(String cycles) {
        cycles = cycles.replaceAll("\\s+", "");
        cycles = cycles.replaceAll("\\)", "");
        cycles = cycles.replaceFirst("\\(", "");
        Scanner s = new Scanner(cycles).useDelimiter("\\(");
        ArrayList<String> splitCycles = new ArrayList<>();
        while (s.hasNext()) {
            splitCycles.add(s.next());
        }
        return splitCycles;
    }



    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int r = wrap(p);
        return _alphabet.toInt(permute(_alphabet.toChar(r)));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int r = wrap(c);
        return _alphabet.toInt(this.invert(_alphabet.toChar(r)));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return charMap.get(p);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return reverseCharMap.get(c);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < cycleList.size(); i++) {
            if (cycleList.get(i).length() == 1) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Hashmap that maps the permutation forward. */
    private HashMap<Character, Character> charMap = new HashMap<>();
    /** Hashmap that maps the permutation backwards. */
    private HashMap<Character, Character> reverseCharMap = new HashMap<>();
    /** ArrayList that contains all the cycles. */
    private ArrayList<String> cycleList;
}

