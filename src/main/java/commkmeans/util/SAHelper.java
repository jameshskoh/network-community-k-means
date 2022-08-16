package commkmeans.util;

import java.util.*;
import java.util.stream.Stream;

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
        Map<Integer, Set<Integer>> newCommSets = new HashMap<>();

        for (int c : commSets.keySet()) {
            if (commSets.get(c).size() == 1) {
                oldToNew.put(c, c);
            }

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

            newCommSets.put(newC, commSet);
        }

        return newCommSets;
    }

    // calculate modularity given set information
    public static double calcEnergy(
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> neighborSets,
            double[][] expWeight, int numEdges, boolean parallel) {
        double sum = 0;

        if (parallel) {
            Stream<Set<Integer>> commSetStream = commSets.values().parallelStream();
            sum += commSetStream.mapToDouble(
                    commSet -> calcEnergyIter(commSet, neighborSets, expWeight)).sum();
        } else {
            for (Set<Integer> commSet : commSets.values()) {
                sum += calcEnergyIter(commSet, neighborSets, expWeight);
            }
        }

        return -sum / (2.0 * numEdges);
    }

    private static double calcEnergyIter(
            Set<Integer> commSet, Map<Integer, Set<Integer>> neighborSets,
            double[][] expWeight) {
        double subTotal = 0;

        for (int node : commSet) {
            Set<Integer> neighbors = neighborSets.get(node);

            for (int friend : commSet) {
                subTotal += neighbors.contains(friend) ? 1.0 : 0.0;
                subTotal -= expWeight[node][friend];
            }
        }

        return subTotal;
    }
}
