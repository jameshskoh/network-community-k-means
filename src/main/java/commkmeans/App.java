package commkmeans;

import com.github.fommil.netlib.BLAS;
import commkmeans.graph.Graph;
import commkmeans.util.GraphLoader;
import commkmeans.util.Parameters;

import java.util.Scanner;

public class App {
    private static final String NO_CACHE = "NOCACHE";
    private static final String CACHE_ONLY = "CACHEONLY";

    public static void main(String[] args) {
        boolean noCache = false;
        boolean cacheOnly = false;

        for (String arg : args) {
            if (NO_CACHE.equalsIgnoreCase(arg)) {
                noCache = true;
                System.out.println("Running no-cache mode, cache will be refreshed before solving the problem.");
            } else if (CACHE_ONLY.equalsIgnoreCase(arg)) {
                cacheOnly = true;
                System.out.println("Running cache-only mode, cache will be regenerated.");
            }
        }

        System.out.println(BLAS.getInstance().getClass().getName());

        System.out.println("Please enter your .edge file.");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();
        sc.close();

        // Initializing data
        long startTime = System.nanoTime();
        String cacheName = fileName + ".cache";
        Graph g = new Graph();

        try {
            GraphLoader.loadGraph(g, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        double T_max = 0.2;
        double T_min = 0.002;
        int N_max = g.getNumVertex() / 3;
        int N_min = 2;
        double alpha = 0.99;
        int R = 25;

        Parameters param = new Parameters(T_max, T_min, N_max, N_min, alpha, R);
        Solver sv = new Solver(g, param, cacheName, noCache, cacheOnly);

        long timeInitialize = System.nanoTime();
        String msg1 = String.format("Data initialization took: %.4f s", (timeInitialize - startTime) / 1.0E9);
        System.out.println(msg1);

        if (cacheOnly) return;

        // Solving the problem
        Solution s = sv.solve();
        long timeFinish = System.nanoTime();

        s.printSolution();

        String msg2 = String.format("Solving took: %.4f s", (timeFinish - timeInitialize) / 1.0E9);
        System.out.println("\n\n");
        System.out.println(msg1);
        System.out.println(msg2);

    }
}
