package commkmeans.util;

import java.util.*;

public class PerturbHelper {
    public static Map<Integer, Set<Integer>> perturb(
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> neighborSets,
            double[][] lambda, Random random) {
        int result = random.nextInt(3);

        if (result == 0) {
            return doNothing(commSets);
        } else if (result == 1) {
            return deleteCenter(commSets, neighborSets, lambda);
        } else {
            return addCenter(commSets, neighborSets, lambda);
        }
    }

    public static Map<Integer, Set<Integer>> doNothing(Map<Integer, Set<Integer>> commSets) {
        return commSets;
    }

    public static Map<Integer, Set<Integer>> deleteCenter(
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> neighborSets,
            double[][] lambda) {
        int weakestComm = calcWeakestComm(commSets, neighborSets);

        List<Integer> centers = new ArrayList<>();

        for (int c : commSets.keySet()) {
            if (c == weakestComm) continue;
            centers.add(c);
        }

        return clusterNodes(centers, lambda);
    }

    public static Map<Integer, Set<Integer>> addCenter(
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> neighborSets,
            double[][] lambda) {
        int weakestComm = calcWeakestAvgComm(commSets, neighborSets);

        double minDiss = Double.POSITIVE_INFINITY;
        int newC = 0;

        for (int node : commSets.get(weakestComm)) {
            double diss = lambda[node][weakestComm];

            if (diss < minDiss) {
                minDiss = diss;
                newC = node;
            }
        }

        List<Integer> centers = new ArrayList<>();
        centers.add(newC);


        for (int c : commSets.keySet()) {
            centers.add(c);
        }

        return clusterNodes(centers, lambda);
    }

    private static int calcWeakestComm(
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> neighborSets) {
        int weakest = 0;
        double minStr = Double.POSITIVE_INFINITY;

        for (int c : commSets.keySet()) {
            double str = calcCommStr(commSets.get(c), neighborSets);

            if (str < minStr) {
                minStr = str;
                weakest = c;
            }
        }

        return weakest;
    }

    private static int calcWeakestAvgComm(
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> neighborSets) {
        int weakest = 0;
        double minAvgStr = Double.POSITIVE_INFINITY;

        for (int c : commSets.keySet()) {
            double avgStr = calcCommStr(commSets.get(c), neighborSets) / commSets.get(c).size();

            if (avgStr < minAvgStr) {
                minAvgStr = avgStr;
                weakest = c;
            }
        }

        return weakest;
    }

    private static double calcCommStr(
            Set<Integer> commSet, Map<Integer, Set<Integer>> neighborSets) {
        double sum = 0;

        for (int node : commSet) {
            for (int neighbor : neighborSets.get(node)) {
                if (commSet.contains(neighbor)) {
                    sum += 1.0;
                } else {
                    sum -= 1.0;
                }
            }
        }

        return sum;
    }

    // calculate nearest set given a node
    public static Map<Integer, Set<Integer>> clusterNodes (
            List<Integer> centers, double[][] lambda) {
        Map<Integer, Set<Integer>> commSets = new HashMap<>();
        int numVert = lambda.length;

        for (int c : centers) {
            commSets.put(c, new HashSet<>());
        }

        for (int node = 0; node < numVert; node++) {
            int center = clusterNodesIter(centers, node, lambda);

            Set<Integer> set = commSets.get(center);
            set.add(node);
        }

        return commSets;
    }

    private static int clusterNodesIter (
            List<Integer> centers, int node, double[][] lambda) {
        double minDiss = Double.POSITIVE_INFINITY;
        int minCenter = 0;

        for (int c : centers) {
            double diss = lambda[node][c];

            if (diss < minDiss) {
                minDiss = diss;
                minCenter = c;
            }
        }

        return minCenter;
    }
}
