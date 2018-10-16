import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
    private static String DEBUG_FILE = "", SEED_FILE = "", INPUT_FILE = "", OUT_FILE = "";
    private static int SEED_AMT = 900, TABLE_LENGTH = 1009, INPUT_AMT = 50;
    private static int[] seedNums, inputNums;
    private static Hashtable<Integer, Integer> table;

    private static void parseArgs(String[] args) {
        Iterator<String> it = Arrays.asList(args).iterator();
        while(it.hasNext()) {
            try {
                String param = it.next();
                String value = it.next();
                switch (param) {
                    case "OUT_FILE"     : OUT_FILE = value;
                    case "DEBUG_FILE"   : DEBUG_FILE = value;
                    case "SEED_FILE"    : SEED_FILE  = value;
                    case "INPUT_FILE"   : INPUT_FILE = value;
                    case "SEED_AMT"     : SEED_AMT = Integer.parseInt(value);
                    case "TABLE_LENGTH" : TABLE_LENGTH = Integer.parseInt(value);
                    case "ADD_AMT"      : INPUT_AMT = Integer.parseInt(value);
                }
            } catch (Exception ignored) {}
        }
    }

    private static void parseSeeds() {
        Scanner in = in(Input.SEED);
        seedNums = new int[SEED_AMT];
        for (int i = 0; i < seedNums.length; i++)
            seedNums[i] = in.nextInt();
    }

    private static void parseAdditions() {
        Scanner in = in(Input.INPUT);
        inputNums = new int[INPUT_AMT];
        for (int i = 0; i < inputNums.length; i++)
            inputNums[i] = in.nextInt();
    }

    private static void openAddressHash() {

    }

    private static int linearProbing() {
        int numProbes = 0;

        return numProbes;
    }

    private static int quadraticProbing() {
        int numProbes = 0;

        return numProbes;
    }

    private static int doubleHashing() {
        int numProbes = 0;

        return numProbes;
    }

    public static void main(String[] args) {
        if (args.length > 0) parseArgs(args);
        parseSeeds();
        parseAdditions();
        openAddressHash();
        out().println(
                "probing method     | number probes      \n" +
                "-------------------+--------------------\n" +
                "linear             |" + linearProbing() + "\n" +
                "quadratic          |" + quadraticProbing() + "\n" +
                "double hashing     |" + doubleHashing()
        );
    }

    private enum Input {
        DEBUG, SEED, INPUT;
    }

    private static PrintWriter out() {
        PrintWriter out = new PrintWriter(System.out);
        try {
            out = new PrintWriter(new File(OUT_FILE));
        } catch (FileNotFoundException ignored) { }
        return out;
    }

    private static Scanner in(Input type) {
        Scanner in = new Scanner(System.in);
        try {
            switch(type) {
                case DEBUG : return new Scanner(new File(DEBUG_FILE));
                case SEED  : return new Scanner(new File(SEED_FILE));
                case INPUT : return new Scanner(new File(INPUT_FILE));
            }
        } catch (FileNotFoundException ignored) { }
        return in;
    }
}
