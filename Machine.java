package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;


import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Ryan Chen
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        if (numRotors < 1) {
            throw error("Can't have less than 1 rotor");
        } else if (_numPawls == _numRotors) {
            throw error("Number of pawls and rotors can't be the same");
        }

        _allRotors = allRotors;
        _notchStatus = new boolean[_numRotors];
        _rotorSlots = new ArrayList<>();
        setRotorKey();
    }

    /** Set a HashMap with the name of the rotor as the key and
     * the corresponding rotor as the value. */
    void setRotorKey() {
        rotorKey = new HashMap<String, Rotor>();
        for (Rotor temp : _allRotors) {
            getRotorKey().put(temp.name(), temp);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != numRotors()) {
            throw error("Number of rotors trying to be inserted "
                    + "does not equal the number of slots");
        }
        for (int i = 0; i < rotors.length; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i] != null && rotors[i].equals(rotors[j])) {
                    throw error("There are duplicate rotors");
                }
            }
        }
        Rotor currentRotor; numMove = 0;
        for (int i = 0; i < rotors.length; i++) {
            if (!(rotorKey.containsKey(rotors[i]))) {
                throw error("Rotor is not in the rotor slot");
            }
            currentRotor = rotorKey.get(rotors[i]);
            if ((i == 0) && (!currentRotor.reflecting())) {
                throw error("Reflector slot not filled by a reflector");
            } else if (i != 0 && currentRotor.reflecting()) {
                throw error("Reflector in a slot for a fixed/moving rotor");
            }
            if (currentRotor.rotates()) {
                numMove++;
            }
            if (numMove > numPawls()) {
                throw error("Number of moving rotors "
                        + "cannot exceed that of the pawls");
            }
            _rotorSlots.add(currentRotor);
        }
        noMoreMove = false;
        for (int k = rotors.length - 1; k >= 0; k--) {
            currentRotor = rotorKey.get(rotors[k]);
            if (k == rotors.length - 1 && !currentRotor.rotates()) {
                throw error("Furthest right rotor must be a moving rotor");
            }
            if (currentRotor.rotates() && noMoreMove) {
                throw error("You can't put a moving rotor to the "
                        + "left of a fixed one");
            } else if (!currentRotor.rotates()) {
                noMoreMove = true;
            }
        }
    }

    /** Empty the rotor slots. */
    void emptyRotors() {
        _rotorSlots.clear();
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    public void setRotors(String setting) {
        if (numRotors() - 1 != setting.length()) {
            throw error("Setting length is not equal to the number of rotors");
        }
        for (int i = 0; i < setting.length(); i++) {
            if (!(_alphabet.contains(setting.charAt(i)))) {
                throw error("Setting contains a string not in the alphabet");
            }
        }
        char[] hello = setting.toCharArray();
        for (int i = 1; i <= setting.length(); i++) {
            _rotorSlots.get(i).set(hello[i - 1]);
        }
        if (!(_rotorSlots.get(0).reflecting())) {
            throw error("The first rotor is not a reflector");
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceMachine();
        int output = _plugboard.permute(c);
        for (int i = _numRotors - 1; i >= 0; i--) {
            output = _rotorSlots.get(i).convertForward(output);
        }
        for (int j = 1; j < _numRotors; j++) {
            output = _rotorSlots.get(j).convertBackward(output);
        }
        return _plugboard.permute(output);
    }

    /** Advances the Machine. */
    void advanceMachine() {
        updateNotchStatus();
        _rotorSlots.get(_numRotors - 1).advance();
        _rotorSlots.get(_numRotors - 1).setHasMoved(true);
        for (int i = numRotors() - 1; i > 1; i--) {
            if (_rotorSlots.get(i - 1).rotates() && _notchStatus[i]) {
                _rotorSlots.get(i).advance();
                _rotorSlots.get(i).setHasMoved(true);
                _rotorSlots.get(i - 1).advance();
                _rotorSlots.get(i - 1).setHasMoved(true);
            }
        }
        for (int i = numRotors() - 1; i > 0; i--) {
            _rotorSlots.get(i).setHasMoved(false);
        }
    }

    /** Updates the string that has all of the notch statuses. */
    void updateNotchStatus() {
        for (int i = 0; i < numRotors(); i++) {
            _notchStatus[i] = _rotorSlots.get(i).atNotch();
        }
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String msgCopy = msg.replaceAll(" ", "");
        String[] msgArray = new String [msgCopy.length()];
        for (int i = 0; i < msgCopy.length(); i++) {
            if (!(_alphabet.contains(msgCopy.charAt(i)))) {
                throw error("This message has a letter that is not "
                        + "in the Alphabet");
            }
            int input = _alphabet.toInt(msgCopy.charAt(i));
            int output = convert(input);
            msgArray[i] = Character.toString(_alphabet.toChar(output));
        }
        String result = "";
        for (int j = 0; j < msgArray.length; j++) {
            result += msgArray[j];
        }
        return result;
    }

    /** A function to return the rotorKey. */
    public HashMap<String, Rotor> getRotorKey() {
        return rotorKey;
    }
    /** A function to return rotorSlots. */
    public ArrayList<Rotor> getRotorSlots() {
        return _rotorSlots;
    }
    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** The number of rotors. */
    private int _numRotors;
    /** The number of pawls. */
    private int _numPawls;
    /** The status of each notch (whether or not each rotor is at a notch). */
    private boolean [] _notchStatus;
    /** A collection of all of the rotors. */
    private Collection<Rotor> _allRotors;
    /** The rotor slots for the machine. */
    private ArrayList<Rotor> _rotorSlots;
    /** A hashmap mapping each name to the corresponding rotor.*/
    private HashMap<String, Rotor> rotorKey;
    /** The permutation representing the plugboard. */
    private Permutation _plugboard;
    /** The boolean representing the whether a fixed rotor has been placed. */
    private boolean noMoreMove;
    /** The int representing the number of moving rotors. */
    private int numMove;
}

