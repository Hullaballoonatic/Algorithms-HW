import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {
    private static File INPUT_FILE = new File("input.txt");
    private static String OUTPUT_FP = "output.txt";

    private static PrintWriter out() {
        PrintWriter out = new PrintWriter(System.out);
        try {
            out = new PrintWriter(OUTPUT_FP);
        } catch (FileNotFoundException ignored) { }
        return out;
    }

    private static Scanner in() {
        Scanner in = new Scanner(System.in);
        try {
            return new Scanner(INPUT_FILE);
        } catch (FileNotFoundException ignored) { }
        return in;
    }

    public static void main(String ... args) {
        if (args.length >= 2) {
            OUTPUT_FP = args[args.length - 1];
            INPUT_FILE = new File(args[args.length - 2]);
        }

        Scanner in = in();

        int SEED_LIMIT = 900, INPUT_LIMIT = 50;

        int[] seedNums = new int[SEED_LIMIT], inputNums = new int[INPUT_LIMIT];

        for (int i = 0; i < SEED_LIMIT; i++)
            seedNums[i] = in.nextInt();
        for (int i = 0; i < INPUT_LIMIT; i++)
            inputNums[i] = in.nextInt();

        String output = String.format(
                "probing method     | number probes      \n" +
                "-------------------+--------------------\n" +
                "linear             | %s\n" +
                "quadratic          | %s\n" +
                "double hashing     | %s\n",
                new LinearProbing(seedNums, inputNums).toString(),
                new QuadraticProbing(seedNums, inputNums).toString(),
                new DoubleHashing(seedNums, inputNums).toString());

        out().print(output);
        out.print(output);
    }
}

abstract class OpenAddressing {
    private Hashtable<Integer, Integer> table = new Hashtable<>(TABLE_SIZE);

    private int probeCount = 0;

    static int TABLE_SIZE = 1009;

    int h(int k) {
        return k % TABLE_SIZE;
    }
    abstract int h(int k, int i);
    private int insert(int value) {
        for (int probe = 0, key = h(value, probe); probe <= TABLE_SIZE; probe++, key = h(value, probe)) {
            if (table.get(key) == null) {
                table.put(key, value);
                return probe;
            }
        }
        return TABLE_SIZE;
    }

    @Override
    public String toString() {
        return "" + probeCount;
    }

    OpenAddressing(int[] seedNums, int[] inputNums) {
        for (int num : seedNums)
            insert(num);
        for (int num : inputNums)
            probeCount += insert(num);
    }
}

class LinearProbing extends OpenAddressing {
    LinearProbing(int[] seedNums, int[] inputNums) {
        super(seedNums, inputNums);
    }

    int h(int k, int i) {
        return (h(k) + i) % TABLE_SIZE;
    }
}

class QuadraticProbing extends OpenAddressing {
    QuadraticProbing(int[] seedNums, int[] inputNums) {
        super(seedNums, inputNums);
    }

    int h(int k, int i) {
        int c1 = 1, c2 = 1;
        return (h(k) + c1*i + c2*i*i);
    }
}

class DoubleHashing extends OpenAddressing {
    private static final int m = 997;

    DoubleHashing(int[] seedNums, int[] inputNums) {
        super(seedNums, inputNums);
    }

    private int h1(int k) {
        return h(k);
    }

    private int h2(int k) {
        return 1 + (k % m);
    }
    int h(int k, int i) {
        return (h1(k) + i*h2(k)) % m;
    }
}