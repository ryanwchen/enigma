package enigma;
import java.util.ArrayList;
/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Ryan Chen
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = chars;
        for (char i : chars.toCharArray()) {
            alphaChars.add(i);
        }
    }


    /** Returns the char in the alphabet corresponding to the index.
     * @param index The index that you are using.
     **/
    char get(int index) {
        return alphaChars.get(index);
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return alphaChars.size();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        return alphaChars.contains(Character.valueOf(ch));
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return alphaChars.get(index);
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return alphaChars.indexOf(ch);
    }

    /** The ArrayList that represents the Alphabet. */
    private ArrayList<Character> alphaChars = new ArrayList<>();

    /** The string of the alphabet. */
    private String _chars;

    /** Return the string of the alphabet. */
    public String getChars() {
        return _chars;
    }
}
