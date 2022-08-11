package commkmeans.util;

import java.util.*;

public class SAHelper {
    // generate random sets
    public static Map<Integer, Set<Integer>> calcRandomSets(
            int numSet, int numVert, Random random) {
        List<Integer> candidates = new ArrayList<>();

        Map<Integer, Set<Integer>> randomSets = new HashMap<>();

        for (int i = 0; i < numVert; i++) {
            candidates.add(i);
        }

        Collections.shuffle(candidates, random);

        for (int i = 0; i < numSet; i++) {
            Set<Integer> members = new HashSet<>();

            for (int j = i; j < numVert; j += numSet) {
                members.add(candidates.get(j));
            }

            randomSets.put(candidates.get(i), members);
        }

        return randomSets;
    }

    // calculate center given set information
    public static Map<Integer, Set<Integer>> calcRecenter(
            Map<Integer, Set<Integer>> commSets, double[][] lambda) {
        Map<Integer, Integer> oldToNew = new HashMap<>();

        for (int c : commSets.keySet()) {
            double minDiss = Double.POSITIVE_INFINITY;
            int newC = 0;

            for (int node : commSets.get(c)) {
                double diss = 0;

                for (int friend : commSets.get(c)) {
                    if (node == friend) continue;

                    diss += lambda[node][friend];
                }

                if (diss < minDiss) {
                    minDiss = diss;
                    newC = node;
                }
            }

            oldToNew.put(c, newC);
        }

        for (int c : oldToNew.keySet()) {
            int newC = oldToNew.get(c);

            Set<Integer> commSet = commSets.get(c);

            commSets.put(newC, commSet);
            commSets.remove(c);
        }

        return commSets;
    }

    // calculate modularity given set information
    public static double calcEnergy(
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> neighborSets,
            double[][] expWeight, int numEdges) {
        double sum = 0;

        for (Set<Integer> commSet : commSets.values()) {
            for (int node : commSet) {
                Set<Integer> neighbors = neighborSets.get(node);

                for (int friend : commSet) {
                    sum += neighbors.contains(friend) ? 1.0 : 0.0;
                    sum -= expWeight[node][friend];
                }
            }
        }

        return -sum / (2.0 * numEdges);
    }
}
