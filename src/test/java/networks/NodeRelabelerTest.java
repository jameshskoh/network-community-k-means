package networks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NodeRelabelerTest {
    static Stream<Arguments> values() {
        Map<Integer, Set<Integer>> neighborSets1 = new LinkedHashMap<>();
        neighborSets1.put(3, Stream.of(8, 21).collect(Collectors.toSet()));
        neighborSets1.put(8, Stream.of(3, 6).collect(Collectors.toSet()));
        neighborSets1.put(6, Stream.of(8).collect(Collectors.toSet()));
        neighborSets1.put(21, Stream.of(3, 40).collect(Collectors.toSet()));
        neighborSets1.put(40, Stream.of(21).collect(Collectors.toSet()));

        Map<Integer, Set<Integer>> newNeighborSets1 = new HashMap<>();
        newNeighborSets1.put(0, Stream.of(1, 3).collect(Collectors.toSet()));
        newNeighborSets1.put(1, Stream.of(0, 2).collect(Collectors.toSet()));
        newNeighborSets1.put(2, Stream.of(1).collect(Collectors.toSet()));
        newNeighborSets1.put(3, Stream.of(0, 4).collect(Collectors.toSet()));
        newNeighborSets1.put(4, Stream.of(3).collect(Collectors.toSet()));

        return Stream.of(
                Arguments.arguments(neighborSets1, newNeighborSets1)
        );
    }


    @ParameterizedTest
    @MethodSource("values")
    void relabel_shouldRelabelInOrder(
            Map<Integer, Set<Integer>> neighborSets, Map<Integer, Set<Integer>> newNeighborSets) {
        Map<Integer, Set<Integer>> myNewNeighborSets = NodeRelabeler.relabel(neighborSets);

        assertEquals(neighborSets.size(), newNeighborSets.size());

        for (int node : newNeighborSets.keySet()) {
            assertTrue(myNewNeighborSets.containsKey(node));

            for (int neighbor : newNeighborSets.get(node)) {
                assertTrue(myNewNeighborSets.get(node).contains(neighbor));
            }
        }
    }
}
