/*
  ALGORITHMS: Homework 9
  Written by: Casey Stratton

  Problem Definition
  ------------------
  In our study of history at the University of Arkansas, we noticed that before
  the money or currency was introduced, barter systems were used. For
  instance, during a certain time window, one kg (kilogram) of corn can be
  exchanged for 0.9 kg of wheat, 0.8 kg of wheat can be exchanged for 1.2 kg
  millet (0.9 kg of wheat can be exchanged for 1.35 kg of millet), one kg millet
  can be exchanged for 1.5 kg of sorghum, and one kg of sorghum can be exchanged
  for 0.5 kg of corn. Observe that in this example, through a sequence
  of transactions, one kg corn may be exchanged for 1.0125 kg of corn! That
  is, 1 kg of corn gets 0.9 kg of wheat, which gets 1.35 kg of millet, which gets
  2.025 kg of sorghum, which gets 1.0125 kg of corn. We notice the possibility
  that in a barter system one may be able to generate a profit by simply going
  through a sequence of exchanges! A barter system with this phenomenon is
  called inefficient. Note that these conversion values may change over time
  and in this example, not all the conversion values between agriculture products
  are shown and not all the products are shown (for example, rice and
  barley).

  As a student of algorithms, we would like to abstract a barter system as
  follows. We use integer 1 to n to name the products of the system. If x kg
  of product i can be exchanged for y kg of product j, we will have an entry
  i j x y. If no market for or no one wants to exchange from product i to
  product j then no such entry exists in the system. As we have learned many
  algorithms and gained skills of develop them, we would like to challenge
  ourselves to develop an efficient algorithm to determine if a given barter
  system is inefficient or not. If the system is inefficient, we need to give a
  sequence of exchanges that demonstrates a profit.

  Argue the correctness of your algorithm, analyze the running time of your
  algorithm for barter system efficiency analysis. Please do the same for the
  algorithm that discovers a sequence of exchanges that results a profit if such
  a sequence exists.


  Input file format for a barter system
  -------------------------------------
  n            : n is the number of products and products are 1 to n
  i j x y      : i and j are products and x y kg floating point value
  . . .        : each unique i and j appear at most once
  . . .        : the quadruples end by EOF


  Output file format for a barter system analysis
  -----------------------------------------------
  yes or no    : yes for inefficient system, and no otherwise
  i j x y      : start of a profit sequence for an inefficient system
  j k u v      :
  . . .        :
  p i w z      : end of the profit sequence
  one kg of i gets x (x > 1) kg of i from the above sequence


  Example Input                Example Output
  -------------                --------------
  5                            yes
  1 2 1 0.9                    1 2 1 0.9
  2 3 0.8 1.2                  2 3 0.8 1.2
  3 4 1 1.5                    3 4 1 1.5
  4 1 1 0.5                    4 1 1 0.5
  2 1 0.9 9.98                 one kg of 1 gets 1.0125 kg of 1 from the above sequence.
  1 5 1 0.7
  5 1 0.6 0.8


  Approach
  --------
  A inefficient barter system is any which, when represented as a graph, contains a negative cycle.
  A negative edge in this case is any for which product mass is gained upon traversal instead of lost, i.e.
  negative edge:  1.0
  positive edge: < 1.0
 */

import java.io.*;
import java.util.*;

/**
 * Edge
 * x kg of product i can be traded for y kg of product j
 */
class Edge {
    int i, j;
    private double x, y;

    double exchange(double barterMass) {
        return barterMass * y / x;
    }

    Edge(Scanner in) {
        i = in.nextInt() - 1;
        j = in.nextInt() - 1;
        x = in.nextDouble();
        y = in.nextDouble();
    }

    @Override
    public String toString() {
        return String.format("%d %d %f %f", i + 1, j + 1, x, y);
    }
}

/**
 * Main
 * n : number of products
 * G : Adjacency Matrix describing exchange rate between products
 * E : List of all edges in the Graph
 */
class Homework9 {
    private static final double oo = -1.0;
    private static int n;
    private static final ArrayList<Edge> E = new ArrayList<>();

    private static Edge getEdge(int i, int j) {
        for (Edge e : E)
            if (e.i == i && e.j == j)
                return e;
        return null;
    }

    /**
     * bellmanFord
     * Time complexity: O(V * E)
     * Space complexity: O(V)
     * An implementation of the Bellman-Ford used to detect a negative cycle.
     * <p>
     * This algorithm differs in specifically what it is we are measuring and how we compare it.
     * As opposed to the typical measure of distance, we are here measuring and comparing mass.
     * A weight is more costly not if it covers greater distance, but if it results in fewer mass.
     * Thus, a negative edge is one in which mass is gained.
     *
     * @return : the negative cycle represented by a sequence of edges
     */
    private static Result bellmanFord() {
        // initialize
        double[] mass = new double[n];
        int[] predecessor = new int[n];
        Arrays.fill(mass, oo);
        Arrays.fill(predecessor, -1);
        mass[0] = 1.0;

        // relaxing all takes n^2 time
        for (int i = 1; i < n; i++)
            for (Edge e : E)
                if (e.exchange(mass[e.i]) > mass[e.j]) {
                    mass[e.j] = e.exchange(mass[e.i]);
                    predecessor[e.j] = e.i;
                }

        // check for negative cycle takes at most n time
        for (Edge e : E)
            if (e.exchange(mass[e.i]) > mass[e.j]) // has negative cycle
                return new Result(mass, predecessor, e.i);

        return null;
    }

    private static class Result {
        double[] mass;
        int[] predecessor;
        int start;
        double exchangeResult = 1.0;
        Stack<Edge> sequence = new Stack<>();

        Result(double[] mass, int[] predecessor, int start) {
            this.mass = mass;
            this.predecessor = predecessor;
            this.start = start;

            // building the sequence takes n*E time
            int cur = start;
            int pre = predecessor[start];
            do {
                Edge edge = getEdge(pre, cur);
                sequence.add(edge);
                exchangeResult = edge.exchange(exchangeResult);
                cur = pre;
                pre = predecessor[cur];
            } while (cur != start);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("yes").append("\n");
            while (!sequence.isEmpty())
                sb.append(sequence.pop()).append("\n");
            sb.append(String.format("one kg of product %d gets %.3f kg of product %d from the above sequence.", start + 1, exchangeResult, start + 1));
            return sb.toString();
        }
    }

    private static Scanner getInputStream(String fp) {
        try {
            return new Scanner(new File(fp));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Scanner(System.in);
        }
    }

    private static PrintStream getOutputStream(String fp) {
        try {
            return new PrintStream(new File(fp));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return System.out;
        }
    }

    public static void main(String... args) {
        Scanner in = getInputStream(args.length >= 1 ? args[0] : "input.txt");
        PrintStream out = getOutputStream(args.length >= 2 ? args[1] : "output.txt");

        // parsing input
        n = in.nextInt();
        while (in.hasNext())
            E.add(new Edge(in));

        // computing
        Result result = bellmanFord();

        // printing output
        out.print(result != null ? result : "no");
    }
}