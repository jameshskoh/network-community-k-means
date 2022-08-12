package networks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NodeRelabeler {
    public static Map<Integer, Set<Integer>> relabel(Map<Integer, Set<Integer>> neighborSets) {
        Map<Integer, Integer> remap = new HashMap<>();

        int newIndex = 0;

        for (int node : neighborSets.keySet()) {
            remap.put(node, newIndex);
            newIndex++;
        }

        Map<Integer, Set<Integer>> newNeighborSets = new HashMap<>();

        for (int node : neighborSets.keySet()) {
            Set<Integer> newNeighbors = new HashSet<>();

            for (int neighbor : neighborSets.get(node)) {
                newNeighbors.add(remap.get(neighbor));
            }

            newNeighborSets.put(remap.get(node), newNeighbors);
        }

        return newNeighborSets;
    }
}
