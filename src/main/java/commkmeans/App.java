package commkmeans;

import com.github.fommil.netlib.BLAS;
import commkmeans.graph.Graph;
import commkmeans.util.GraphLoader;
import commkmeans.util.Parameters;

import java.io.IOException;
import java.util.Scanner;

public class App {
    private static final String NO_CACHE = "NOCACHE";
    private static final String CACHE_ONLY = "CACHEONLY";

    public static void main(String[] args) {
        boolean noCache = false;
        boolean cacheOnly = false;
        String inputFileName = null;

        for (String arg : args) {
            if (NO_CACHE.equalsIgnoreCase(arg)) {
                noCache = true;
                System.out.println("Running no-cache mode, cache will be refreshed before solving the problem.");
            } else if (CACHE_ONLY.equalsIgnoreCase(arg)) {
                cacheOnly = true;
                System.out.println("Running cache-only mode, cache will be regenerated.");
            } else if (arg.endsWith(".edge")) {
                inputFileName = arg;
            }
        }

        if (inputFileName == null) {
            System.out.println(BLAS.getInstance().getClass().getName());
            System.out.println("Please enter your .edge file.");
            Scanner sc = new Scanner(System.in);
            inputFileName = sc.nextLine();
            sc.close();
        }

        // Initializing data
        long startTime = System.nanoTime();
        String cacheName = inputFileName + ".cache";
        String exportFileName = inputFileName + ".result";
        Graph g = new Graph();

        try {
            GraphLoader.loadGraph(g, inputFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String configFileName = "config.properties";
        int N_max = g.getNumVertex() / 3;
        int N_min = 2;

        Parameters param = null;
        try {
            param = new Parameters(configFileName, N_max, N_min);
        } catch (IOException e) {
            System.out.println("Configuration not found, fallback to default settings.");
            param = new Parameters(0.2, 0.002, N_max, N_min, 0.99, 25);
        }
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

        // Export solution
        try {
            s.exportSolution(exportFileName, timeInitialize - startTime, timeFinish - timeInitialize);
            String msg = String.format("Result exported successfully! File path: %s", exportFileName);
            System.out.println(msg);
        } catch (IOException e) {
            System.out.println("Result export failed!");
        }
    }
}
