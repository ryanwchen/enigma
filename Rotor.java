package enigma;
import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Ryan Chen
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        if (posn < 0 || posn >= this.size()) {
            throw error("Position exceeds the size of the alphabet");
        } else {
            _setting = posn;
        }
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        if (_permutation.alphabet().contains(cposn)) {
            _setting = _permutation.alphabet().toInt(cposn);
        } else {
            throw error("This character is not in the alphabet of this rotor");
        }
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        if (p < 0 || p >= this.size()) {
            throw error("This integer is not in range");
        } else {
            int output = _permutation.permute(_permutation.wrap(p + _setting));
            return _permutation.wrap((output - _setting));
        }
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        if (e < 0 || e >= this.size()) {
            throw error("This integer is not in range");
        } else {
            int output = _permutation.invert(_permutation.wrap(e + _setting));
            return _permutation.wrap((output - _setting));
        }
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** Sets hasMoved.
     * @param moved Whether or not the rotor has moved.*/
    public void setHasMoved(boolean moved) {
        this._hasMoved = moved;
    }
    /** Returns hasMoved.*/
    public boolean hasMoved() {
        return _hasMoved;
    }
    /** My name. */
    private final String _name;
    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;
    /** The numerical setting of the rotor. */
    private int _setting;
    /** Boolean to check if the rotor has moved. */
    private boolean _hasMoved;
}
