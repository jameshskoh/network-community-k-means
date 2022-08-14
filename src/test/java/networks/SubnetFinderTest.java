package networks;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubnetFinderTest {
    @Test
    void findSubnet_shouldReturnCorrectSubnets() {
        Map<Integer, Set<Integer>> neighborSets = Map.of(
                0, Stream.of(1, 2, 3).collect(Collectors.toSet()),
                1, Stream.of(0, 3).collect(Collectors.toSet()),
                2, Stream.of(0, 3).collect(Collectors.toSet()),
                3, Stream.of(0, 1, 2).collect(Collectors.toSet()),
                4, Stream.of(5).collect(Collectors.toSet()),
                5, Stream.of(4, 6).collect(Collectors.toSet()),
                6, Stream.of(5, 7).collect(Collectors.toSet()),
                7, Stream.of(6).collect(Collectors.toSet())
        );

        List<Set<Integer>> subnets = Stream.of(
                Stream.of(0, 1, 2, 3).collect(Collectors.toSet()),
                Stream.of(4, 5, 6, 7).collect(Collectors.toSet())
        ).collect(Collectors.toList());

        List<Set<Integer>> mySubnets = SubnetFinder.findSubnets(neighborSets);

        assertEquals(subnets.size(), mySubnets.size());

        for (int i = 0; i < subnets.size(); i++) {
            for (int node : subnets.get(i)) {
                assertTrue(mySubnets.get(i).contains(node));
            }
        }
    }

    @Test
    void sortSubnetByDecreasingSize_shouldWorkCorrectly() {
        List<Set<Integer>> subnets = Stream.of(
                Stream.of(0, 1, 2, 3).collect(Collectors.toSet()),
                Stream.of(4, 5, 6, 7, 8).collect(Collectors.toSet()),
                Stream.of(9, 10, 11).collect(Collectors.toSet()),
                Stream.of(12).collect(Collectors.toSet()),
                Stream.of(13, 14, 15).collect(Collectors.toSet())
        ).collect(Collectors.toList());

        subnets = SubnetFinder.sortSubnetByDecreasingSize(subnets);

        assertEquals(5, subnets.get(0).size());
        assertEquals(4, subnets.get(1).size());
        assertEquals(3, subnets.get(2).size());
        assertEquals(3, subnets.get(3).size());
        assertEquals(1, subnets.get(4).size());
    }

    @Test
    void findSubnetNeighborSets_shouldReturnCorrectSubset() {
        Map<Integer, Set<Integer>> neighborSets = new HashMap<>();
        neighborSets.put(0, Stream.of(1, 3).collect(Collectors.toSet()));
        neighborSets.put(1, Stream.of(0, 3).collect(Collectors.toSet()));
        neighborSets.put(2, Stream.of(4).collect(Collectors.toSet()));
        neighborSets.put(3, Stream.of(0, 1).collect(Collectors.toSet()));
        neighborSets.put(4, Stream.of(2).collect(Collectors.toSet()));

        List<Set<Integer>> subnets = Stream.of(
            Stream.of(0, 1, 3).collect(Collectors.toSet()),
            Stream.of(2, 4).collect(Collectors.toSet())
        ).collect(Collectors.toList());

        Map<Integer, Set<Integer>> mySubnetNeighborSets = SubnetFinder.findSubnetNeighborSets(neighborSets, subnets, 0);

        assertEquals(3, mySubnetNeighborSets.size());

        for (int i : new int[]{0, 1, 3}) {
            assertEquals(neighborSets.get(i).size(), mySubnetNeighborSets.get(i).size());

            for (int node : neighborSets.get(i)) {
                assertTrue(mySubnetNeighborSets.get((i)).contains(node));
            }
        }
    }
}
