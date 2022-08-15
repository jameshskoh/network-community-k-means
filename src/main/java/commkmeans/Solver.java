package commkmeans;

import commkmeans.exceptions.InvalidFormatException;
import commkmeans.graph.Graph;
import commkmeans.util.*;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    public Solver(Graph g, Parameters param, String cacheName, boolean noCache, boolean cacheOnly) {
        double[][] cache;
        System.out.println("Initializing solver...");

        neighborSets = g.exportGraph();
        numVertex = g.getNumVertex();
        numEdge = g.getNumEdge();

        System.out.println("Initializing parameters...");

        // For lambda
        nodeDegrees = InitHelper.calcNodeDegrees(neighborSets);
        System.out.println("Node degree: done");

        negPassProbs = InitHelper.calcNegPassProbs(neighborSets, nodeDegrees);
        System.out.println("Passage probability: done");

        // Initialize InitHelper vecUnity, not the best practice, but saves time
        // Only needs to init once for each graph, since all b in Ax=b are the same size and same value
        double[] vecUnity = new double[numVertex];
        for (int i = 0; i < numVertex; i++) vecUnity[i] = 1.0;
        InitHelper.vecUnity = vecUnity;

        if (noCache || cacheOnly) {
            cache = computeAndWriteCache(negPassProbs, cacheName);
        } else {
            try {
                cache = readCache(cacheName, numVertex);
            } catch (FileNotFoundException e) {
                System.out.println("Cache missing or unreadable!");
                System.out.println("Fall back to cache generation.");
                cache = computeAndWriteCache(negPassProbs, cacheName);
            } catch (InvalidFormatException e) {
                System.out.println("Cache is corrupted!");
                System.out.println("Fall back to cache generation.");
                cache = computeAndWriteCache(negPassProbs, cacheName);
            }
        }

        passTime = cache;

        if (cacheOnly) {
            System.out.println("Cache generation complete. Exiting program.");
            lambda = null;
            expWeight = null;
            random = null;
            this.param = param;

            return;
        }

        lambda = InitHelper.calcLambda(passTime);
        System.out.println("Dissimilarity index: done");

        expWeight = InitHelper.calcExpWeight(nodeDegrees, numEdge);
        System.out.println("Expected weight: done");

        this.param = param;

        if (param.seed == -1) {
            random = new Random();
        } else {
            random = new Random(param.seed);
        }
    }

    public Solution solve() {
        System.out.println("Initializing solving procedure...");

        Solution s = new Solution();

        int randomUpperBound = (int)Math.min(Math.sqrt(numVertex/5.0), param.N_max) + 3;

        int N = random.nextInt(randomUpperBound - param.N_min + 1) + param.N_min;

        Map<Integer, Set<Integer>> randomSets = SAHelper.calcRandomSets(N, numVertex, random);
        randomSets = SAHelper.calcRecenter(randomSets, lambda);
        s.optCommSets = randomSets;
        s.optObj = SAHelper.calcEnergy(randomSets, neighborSets, expWeight, numEdge);

        Map<Integer, Set<Integer>> currCommSets = randomSets;
        double currObj = s.optObj;

        double T = param.T_max;
        while (T >= param.T_min) {
            String msg = String.format("Current temperature: %.5f", T);
            System.out.println(msg);

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

        System.out.println("\nDone!\n");

        return s;
    }

    private double[][] readCache(String cacheName, int numVert) throws FileNotFoundException, InvalidFormatException {
        System.out.println("Attempt to read cache.");
        double[][] cache = CacheHelper.readPassTimeCache(cacheName, numVert);
        System.out.println("Cache read");

        return cache;
    }

    private double[][] computeAndWriteCache(double[][] negPassProbs, String cacheName) {
        System.out.println("Computing cache.");
        double[][] cache = InitHelper.calcPassTime(negPassProbs);
        System.out.println("Cache computed.");

        writeCache(cacheName, cache);

        return cache;
    }

    private void writeCache(String cacheName, double[][] cache) {
        System.out.println("Attempt to write cache.\n");

        try {
            CacheHelper.writePassTimeCache(cacheName, cache);
            System.out.println("Cache written.\n");
        } catch (IOException e) {
            System.out.println("Cache writing failed! Please check your file permissions.");
        }
    }
}
