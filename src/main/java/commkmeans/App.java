package commkmeans;

import commkmeans.graph.Graph;
import commkmeans.util.GraphLoader;
import commkmeans.util.Parameters;

public class App {
    public static void main(String[] args) {
        Graph g = new Graph();

        try {
            GraphLoader.loadGraph(g, "data/facebook_ucsd_500_main.edge");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        double T_max = 0.2;
        double T_min = 0.001;
        int N_max = g.getNumVertex() / 3;
        int N_min = 2;
        double alpha = 0.99;
        int R = 35;

        Parameters param = new Parameters(
                T_max, T_min, N_max, N_min, alpha, R);

        Solver sv = new Solver(g, param);
        Solution s = sv.solve();

        s.printSolution();
    }
}
