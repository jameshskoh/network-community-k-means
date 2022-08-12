package networks;

import java.util.*;

public class SubnetFinder {
    public static List<Set<Integer>> findSubnets(Map<Integer, Set<Integer>> neighborSets) {
        List<Set<Integer>> subnets = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        int numVert = neighborSets.size();

        for (int i = 0; i < numVert; i++) {
            if (!visited.contains(i)) {
                Set<Integer> currSet = new HashSet<>();
                subnets.add(currSet);

                Queue<Integer> bfsQueue = new LinkedList<>();
                bfsQueue.add(i);

                while (!bfsQueue.isEmpty()) {
                    int node = bfsQueue.poll();
                    visited.add(node);
                    currSet.add(node);

                    for (int neighbor : neighborSets.get(node)) {
                        if (!visited.contains(neighbor)) {
                            bfsQueue.add(neighbor);
                        }
                    }
                }
            }
        }

        return subnets;
    }

    public static List<Set<Integer>> sortSubnetByDecreasingSize(List<Set<Integer>> subnets) {
        Comparator<Set<Integer>> setSizeComparatorDesc =
                (o1, o2) -> o2.size() - o1.size();

        subnets.sort(setSizeComparatorDesc);

        return subnets;
    }

    public static Map<Integer, Set<Integer>> findSubnetNeighborSets(
            Map<Integer, Set<Integer>> neighborSets, List<Set<Integer>> subnets, int index) {
        Map<Integer, Set<Integer>> subnetNeighborSets = new LinkedHashMap<>();

        Set<Integer> nodeSet = subnets.get(index);

        for (int node : nodeSet) {
            subnetNeighborSets.put(node, neighborSets.get(node));
        }

        return subnetNeighborSets;
    }
}
