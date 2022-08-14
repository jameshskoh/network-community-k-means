package commkmeans;

import com.github.fommil.netlib.BLAS;
import commkmeans.graph.Graph;
import commkmeans.util.GraphLoader;
import commkmeans.util.Parameters;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.println(BLAS.getInstance().getClass().getName());

        System.out.println("Please enter your .edge file.");
        Scanner sc = new Scanner(System.in);

        String fileName = sc.nextLine();

        sc.close();

        long startTime = System.nanoTime();

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

        Parameters param = new Parameters(
                T_max, T_min, N_max, N_min, alpha, R);

        Solver sv = new Solver(g, param);

        long timeInitialize = System.nanoTime();

        Solution s = sv.solve();

        long timeFinish = System.nanoTime();

        s.printSolution();

        String msg1 = String.format("Data initialization took: %.4f s", (timeInitialize - startTime) / 1.0E9);
        String msg2 = String.format("Solving took: %.4f s", (timeFinish - timeInitialize) / 1.0E9);

        System.out.println("\n\n");
        System.out.println(msg1);
        System.out.println(msg2);

    }
}
