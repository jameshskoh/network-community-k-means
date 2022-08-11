package commkmeans.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static commkmeans.util.Constants.EPSILON_COARSE;
import static org.junit.jupiter.api.Assertions.*;

class PerturbHelperTest {
    static Stream<Arguments> values() {
        int randomSeed = 0;

        Map<Integer, Set<Integer>> neighborsSet1 = new HashMap<>();
        neighborsSet1.put(0, Stream.of(1, 4, 5).collect(Collectors.toSet()));
        neighborsSet1.put(1, Stream.of(0, 2, 4).collect(Collectors.toSet()));
        neighborsSet1.put(2, Stream.of(1, 3, 4).collect(Collectors.toSet()));
        neighborsSet1.put(3, Stream.of(2, 4).collect(Collectors.toSet()));
        neighborsSet1.put(4, Stream.of(0, 1, 2, 3, 5).collect(Collectors.toSet()));
        neighborsSet1.put(5, Stream.of(0, 4, 6, 7).collect(Collectors.toSet()));
        neighborsSet1.put(6, Stream.of(5, 7, 8, 9, 10).collect(Collectors.toSet()));
        neighborsSet1.put(7, Stream.of(5, 6).collect(Collectors.toSet()));
        neighborsSet1.put(8, Stream.of(6, 9).collect(Collectors.toSet()));
        neighborsSet1.put(9, Stream.of(6, 8, 10).collect(Collectors.toSet()));
        neighborsSet1.put(10, Stream.of(6, 9).collect(Collectors.toSet()));

        List<Integer> centers1 = Stream.of(4, 7, 9).collect(Collectors.toList());

        Map<Integer, Set<Integer>> commSets1 = new HashMap<>();
        commSets1.put(4, Stream.of(0, 1, 2, 3, 4).collect(Collectors.toSet()));
        commSets1.put(7, Stream.of(5, 6, 7).collect(Collectors.toSet()));
        commSets1.put(9, Stream.of(8, 9, 10).collect(Collectors.toSet()));

        Map<Integer, Set<Integer>> newCommSets1 = new HashMap<>();
        newCommSets1.put(4, Stream.of(0, 1, 2, 3, 4).collect(Collectors.toSet()));
        newCommSets1.put(7, Stream.of(5, 7).collect(Collectors.toSet()));
        newCommSets1.put(9, Stream.of(6, 8, 9, 10).collect(Collectors.toSet()));

        double[][] lambda1 = {
                {0.000000000, 1.117317834, 1.564419192, 1.492118532, 0.847628235, 2.388651194, 5.486139204, 4.031402543, 6.100796761, 6.312600139, 6.100796761},
                {1.117317834, 0.000000000, 0.770968488, 0.609903641, 0.459012839, 3.281495300, 6.294036902, 4.880861271, 6.882481246, 7.089249305, 6.882481246},
                {1.564419192, 0.770968488, 0.000000000, 0.287834482, 0.839432228, 3.670426322, 6.618398059, 5.231007292, 7.195111887, 7.398953540, 7.195111887},
                {1.492118532, 0.609903641, 0.287834482, 0.000000000, 0.831822935, 3.590660038, 6.534038763, 5.144027128, 7.108408051, 7.313500116, 7.108408051},
                {0.847628235, 0.459012839, 0.839432228, 0.831822935, 0.000000000, 3.027807727, 6.054134367, 4.635295413, 6.654442982, 6.861049354, 6.654442982},
                {2.388651194, 3.281495300, 3.670426322, 3.590660038, 3.027807727, 0.000000000, 3.461724873, 1.879789265, 4.157644553, 4.382555205, 4.157644553},
                {5.486139204, 6.294036902, 6.618398059, 6.534038763, 6.054134367, 3.461724873, 0.000000000, 1.787013049, 0.984446953, 1.195413044, 0.984446953},
                {4.031402543, 4.880861271, 5.231007292, 5.144027128, 4.635295413, 1.879789265, 1.787013049, 0.000000000, 2.541426319, 2.780924144, 2.541426319},
                {6.100796761, 6.882481246, 7.195111887, 7.108408051, 6.654442982, 4.157644553, 0.984446953, 2.541426319, 0.000000000, 0.451335467, 0.000000000},
                {6.312600139, 7.089249305, 7.398953540, 7.313500116, 6.861049354, 4.382555205, 1.195413044, 2.780924144, 0.451335467, 0.000000000, 0.451335467},
                {6.100796761, 6.882481246, 7.195111887, 7.108408051, 6.654442982, 4.157644553, 0.984446953, 2.541426319, 0.000000000, 0.451335467, 0.000000000}
        };

        return Stream.of(
                Arguments.arguments(neighborsSet1, centers1, commSets1, newCommSets1, lambda1, randomSeed)
        );
    }

    @ParameterizedTest
    @MethodSource("values")
    void clusterNodes_shouldClusterCorrectly(
            Map<Integer, Set<Integer>> neighborsSet, List<Integer> centers,
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> newCommSets,
            double[][] lambda, int randomSeed) {
        Map<Integer, Set<Integer>> myCommSets = PerturbHelper.clusterNodes(centers, lambda);

        assertEquals(newCommSets.size(), myCommSets.size());

        for (int center : newCommSets.keySet()) {
            assertTrue(myCommSets.containsKey(center));

            for (int node : newCommSets.get(center)) {
                assertTrue(myCommSets.get(center).contains(node));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("values")
    void keepCenter_shouldReclusterComm(
            Map<Integer, Set<Integer>> neighborsSet, List<Integer> centers,
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> newCommSets,
            double[][] lambda, int randomSeed) {
        Map<Integer, Set<Integer>> myCommSets = PerturbHelper.keepCenter(commSets, neighborsSet, lambda);

        assertEquals(newCommSets.size(), myCommSets.size());

        for (int center : newCommSets.keySet()) {
            assertTrue(myCommSets.containsKey(center));

            for (int node : newCommSets.get(center)) {
                assertTrue(myCommSets.get(center).contains(node));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("values")
    void deleteCenter_shouldDeleteWeakestCenter(
            Map<Integer, Set<Integer>> neighborsSet, List<Integer> centers,
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> newCommSets,
            double[][] lambda, int randomSeed) {
        Map<Integer, Set<Integer>> myCommSets = PerturbHelper.deleteCenter(commSets, neighborsSet, lambda);

        assertEquals(commSets.size() - 1, myCommSets.size());

        assertTrue(!myCommSets.containsKey(7) || !myCommSets.containsKey(9));
    }

    @ParameterizedTest
    @MethodSource("values")
    void addCenter_shouldAddToWeakestCenter(
            Map<Integer, Set<Integer>> neighborsSet, List<Integer> centers,
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> newCommSets,
            double[][] lambda, int randomSeed) {
        Map<Integer, Set<Integer>> myCommSets = PerturbHelper.addCenter(commSets, neighborsSet, lambda);

        assertEquals(commSets.size() + 1, myCommSets.size());

        for (int center : commSets.keySet()) {
            assertTrue(myCommSets.containsKey(center));
        }

        assertTrue(myCommSets.containsKey(6));
    }

    @ParameterizedTest
    @MethodSource("values")
    void perturb_shouldPickMethodEqually(
            Map<Integer, Set<Integer>> neighborsSet, List<Integer> centers,
            Map<Integer, Set<Integer>> commSets, Map<Integer, Set<Integer>> newCommSets,
            double[][] lambda, int randomSeed) {
        int iter = 10000;
        double totalCount = 3.0;
        Parameters param = new Parameters(0.1, 0.002, 11/2, 2, 0.93, 100, 0);

        Map<Integer, Set<Integer>> currCommSets = commSets;

        Random random = new Random(randomSeed);

        for (int i = 0; i < iter; i++) {
            currCommSets = PerturbHelper.perturb(currCommSets, neighborsSet, lambda, param, random);
            totalCount += currCommSets.size();
        }
    }
}
