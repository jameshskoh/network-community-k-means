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
    private final Random random;
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

        if (param.seed == -1) {
            random = new Random();
        } else {
            random = new Random(param.seed);
        }
    }

    public Solution solve() {
        Solution s = new Solution();

        int N = random.nextInt(param.N_max - param.N_min + 1) + param.N_min;

        Map<Integer, Set<Integer>> randomSets = SAHelper.calcRandomSets(N, numVertex, random);
        randomSets = SAHelper.calcRecenter(randomSets, lambda);
        s.optCommSets = randomSets;
        s.optObj = SAHelper.calcEnergy(randomSets, neighborSets, expWeight, numEdge);

        Map<Integer, Set<Integer>> currCommSets = randomSets;
        double currObj = s.optObj;

        double T = param.T_max;
        while (T >= param.T_min) {
            int iter = 0;

            while (iter < param.R) {
                currCommSets = PerturbHelper.perturb(currCommSets, neighborSets, lambda, param, random);
                currCommSets = SAHelper.calcRecenter(currCommSets, lambda);
                currObj = SAHelper.calcEnergy(currCommSets, neighborSets, expWeight, numEdge);

                if (currObj <= s.optObj) {
                    s.optCommSets = currCommSets;
                    s.optObj = currObj;
                } else {
                    double prob = random.nextDouble();

                    double eval = Math.exp((currObj - s.optObj) / T);

                    // #TODO
                    if (!(prob < eval)) {
                        // bad luck, revert to previous solution
                        currCommSets = s.optCommSets;
                        currObj = s.optObj;
                    }
                }

                iter++;
            }

            T = T * param.alpha;
        }

        return s;
    }
}
