package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Ryan Chen
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        if (!hasMoved()) {
            set(permutation().wrap(setting() + 1));
        }
    }

    @Override
    boolean atNotch() {
        if (_notches.contains(Character.toString(
                (alphabet().toChar(setting()))))) {
            return true;
        }
        return false;
    }

    /** A string representing the notches. */
    private String _notches;

    /** Let's get my notches! Returns notches. */
    public String getNotches() {
        return _notches;
    }
}
