package commkmeans;

import commkmeans.graph.Graph;
import commkmeans.util.InitHelper;
import commkmeans.util.Parameters;
import commkmeans.util.PerturbHelper;
import commkmeans.util.SAHelper;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Solver {
    private Random random;
    private final Parameters param;
    private final Map<Integer, Set<Integer>> neighborSets;
    private final int numVertex;
    private final int numEdge;

    // For lambda
    private final int[] nodeDegrees;
    private final double[][] negPassProbs;
    private final double[][] passTime;
    private final double[][] lambda;

    // For modularity
    private final double[][] expWeight;

    public Solver(Graph g, Parameters param) {
        neighborSets = g.exportGraph();
        numVertex = g.getNumVertex();
        numEdge = g.getNumEdge();

        // For lambda
        nodeDegrees = InitHelper.calcNodeDegrees(neighborSets);
        negPassProbs = InitHelper.calcNegPassProbs(neighborSets, nodeDegrees);
        passTime = InitHelper.calcPassTime(negPassProbs);
        lambda = InitHelper.calcLambda(passTime);
        expWeight = InitHelper.calcExpWeight(nodeDegrees, numEdge);

        this.param = param;
    }

    public Solution solve() {
        Solution s = new Solution();

        int N = random.nextInt(param.N_min, param.N_max);

        Map<Integer, Set<Integer>> randomSets = SAHelper.calcRandomSets(N, numVertex, random);
        randomSets = SAHelper.calcRecenter(randomSets, lambda);
        s.optCommSets = randomSets;
        s.optObj = SAHelper.calcMod(randomSets, neighborSets, expWeight);

        Map<Integer, Set<Integer>> currCommSets = randomSets;
        double currObj = Double.NEGATIVE_INFINITY;

        double temp = 1000.0;
        // #TODO
        while (temp > 1.0) {
            currCommSets = PerturbHelper.perturb(currCommSets, neighborSets, lambda, random);
            currObj = SAHelper.calcMod(currCommSets, neighborSets, expWeight);

            if (currObj <= s.optObj) {
                s.optCommSets = currCommSets;
                s.optObj = currObj;
            } else {
                double prob = random.nextDouble(0.0, 1.0);

                // #TODO
                if (prob != 0.5) {

                } else {
                    currCommSets = s.optCommSets;
                }
            }

            temp = temp / 2.0;
        }

        return s;
    }
}
