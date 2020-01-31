package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ryan Chen
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        _machine = readConfig();
        String message; String setting;
        hasSetUp = false;
        while (_input.hasNextLine()) {
            setting = _input.nextLine();
            if (setting.matches("(\\s*)?")) {
                message = _machine.convert(setting);
                printMessageLine((message));
            } else if (setting.charAt(0) == '*') {
                hasSetUp = true;
                _machine = setUp(_machine, setting);
            } else if (hasSetUp) {
                message = _machine.convert(setting);
                printMessageLine((message));
            } else {
                throw error("The input is incorrectly formatted");
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.nextLine());
            numRotors = _config.nextInt(); numPawls = _config.nextInt();
            allRotors = new ArrayList<Rotor>();
            while (_config.hasNext(Pattern.compile("[^()*\\s]*"))) {
                allRotors.add(readRotor());
            }
            _machine = new Machine(_alphabet, numRotors, numPawls, allRotors);
            return _machine;
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next("[^()*\\s]*");
            String info = _config.next("[^()*\\s]*");
            String cycles = "";
            while (_config.hasNext(Pattern.compile("(\\([^()*\\s]*\\))*"))) {
                cycles += _config.next(Pattern.compile("(\\([^()*\\s]*\\))*"));
            }
            _permutation = new Permutation(cycles, _alphabet);
            if (info.charAt(0) == 'M') {
                info = info.substring(1);
                rotor = new MovingRotor(name, _permutation, info);
            } else if (info.charAt(0) == 'N') {
                info = info.substring(1);
                rotor = new FixedRotor(name, _permutation);
            } else {
                info = info.substring(1);
                rotor = new Reflector(name, _permutation);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
        return rotor;
    }

    /** Return M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private Machine setUp(Machine M, String settings) {
        Scanner setup = new Scanner(settings);
        if (!(setup.hasNext("\\*"))) {
            throw error("Setting line does not begin with an asterisk");
        } else {
            setup.skip(Pattern.compile("\\*"));
        }
        ArrayList<String> temp = new ArrayList<>();
        while (setup.hasNext("[^()*\\s]*")) {
            temp.add(setup.next("[^()*\\s]*"));
        }
        String ring = ""; String setting;
        if (temp.size() == numRotors + 2) {
            ring = temp.get(numRotors + 1);
            temp.remove(numRotors);
        } else if (temp.size() != numRotors + 1) {
            throw error("Setting contains wrong number of arguments");
        }
        setting = temp.get(numRotors);
        temp.remove(setting);
        rotors = new String[numRotors];
        for (int j = 0; j < numRotors; j++) {
            rotors[j] = temp.get(j);
        }
        String plug = "";
        while (setup.hasNext("(\\([^()*\\s]*\\))*")) {
            plug += setup.next("(\\([^()*\\s]*\\))*");
        }
        plugboard = new Permutation(plug, _alphabet);
        _machine = new Machine(_alphabet, numRotors, numPawls, allRotors);
        _machine.insertRotors(rotors);
        _machine.setRotors(setting);
        _machine.setPlugboard(plugboard);
        if (!(ring.equals("")) && (ring.length() == numRotors - 1)) {
            ring(ring);
        }
        return _machine;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters. */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 6) {
            msg = msg.substring(0, i) + " "
                    + msg.substring(i, msg.length());
        }
        _output.println(msg.trim());
    }

    /** Change the rings.
     * @param ringSetting The setting of the ring.*/
    public void ring(String ringSetting) {
        char[] ringArray = ringSetting.toCharArray();
        int[] setting = new int[ringSetting.length()];
        for (int i = 0; i < ringArray.length; i++) {
            setting[i] = _alphabet.toInt(ringArray[i]);
        }
        for (int i = 1; i < numRotors; i++) {
            Rotor current = _machine.getRotorSlots().get(i);
            int currentSetting = current.setting();
            int ring = setting[i - 1];
            current.set(current.permutation().wrap(currentSetting - ring));
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;
    /** Source of input messages. */
    private Scanner _input;
    /** Source of machine configuration. */
    private Scanner _config;
    /** File for encoded/decoded messages. */
    private PrintStream _output;
    /** The machine. */
    private Machine _machine;
    /** The number of rotors. */
    private int numRotors;
    /** The number of pawls. */
    private int numPawls;
    /** A collection of all rotors. */
    private Collection<Rotor> allRotors;
    /** The permutation of a rotor. */
    private Permutation _permutation;
    /** A rotor. */
    private Rotor rotor;
    /** A string array of rotor names. */
    private String[] rotors;
    /** A permutation representing the plugboard. */
    private Permutation plugboard;
    /** A boolean that tells if a machine has been set up yet. */
    private boolean hasSetUp;
}
