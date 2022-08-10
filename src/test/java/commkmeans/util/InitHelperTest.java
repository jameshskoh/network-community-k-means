package commkmeans.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static commkmeans.util.Constants.EPSILON;
import static org.junit.jupiter.api.Assertions.*;

class InitHelperTest {
    static Stream<Arguments> values() {
        Map<Integer, Set<Integer>> neighborsSet = Map.of(
                0, Stream.of(1, 2, 3).collect(Collectors.toSet()),
                1, Stream.of(0, 3, 4).collect(Collectors.toSet()),
                2, Stream.of(0, 3).collect(Collectors.toSet()),
                3, Stream.of(0, 1, 2).collect(Collectors.toSet()),
                4, Stream.of(1).collect(Collectors.toSet())
        );

        int[] nodeDegrees = {3, 3, 2, 3, 1};

        int numEdges = 12;

        double[][] negPassProbs = {
                {0.0, -1.0/3, -1.0/3, -1.0/3, 0.0},
                {-1.0/3, 0.0, 0.0, -1.0/3, -1.0/3},
                {-1.0/2, 0.0, 0.0, -1.0/2, 0.0},
                {-1.0/3, -1.0/3, -1.0/3, 0.0, 0.0},
                {0.0, -1.0, 0.0, 0.0, 0.0}
        };

        double[][] passTime = {
                {4.0, 4.0, 5.0, 3.0, 15.0},
                {3.5, 4.0, 7.0, 3.5, 11.0},
                {2.5, 5.0, 6.0, 2.5, 16.0},
                {3.0, 4.0, 5.0, 4.0, 15.0},
                {4.5, 1.0, 8.0, 4.5, 12.0}
        };

        double[][] lambda = {
                {0.0, 1.5, 0.5, 0.0, 1.5},
                {1.5, 0.0, Math.sqrt(3.0), 1.5, Math.sqrt(3.0)/3.0},
                {0.5, Math.sqrt(3.0), 0, 0.5, Math.sqrt(24.0)/3.0},
                {0.0, 1.5, 0.5, 0.0, 1.5},
                {1.5, Math.sqrt(3.0)/3.0, Math.sqrt(24.0)/3.0, 1.5, 0.0}
        };

        double[][] weight = {
                {3.0/8.0, 3.0/8.0, 1.0/4.0, 3.0/8.0, 1.0/8.0},
                {3.0/8.0, 3.0/8.0, 1.0/4.0, 3.0/8.0, 1.0/8.0},
                {1.0/4.0, 1.0/4.0, 1.0/6.0, 1.0/4.0, 1.0/12.0},
                {3.0/8.0, 3.0/8.0, 1.0/4.0, 3.0/8.0, 1.0/8.0},
                {1.0/8.0, 1.0/8.0, 1.0/12.0, 1.0/8.0, 1.0/24.0}
        };

        return Stream.of(
                Arguments.arguments(
                        neighborsSet, nodeDegrees, negPassProbs, passTime, lambda, weight, numEdges)
        );
    }

    @ParameterizedTest
    @MethodSource("values")
    void calcNodeDegrees_shouldReturnCorrectNodeDegrees(
            Map<Integer, Set<Integer>> neighborsSet, int[] nodeDegrees,
            double[][] negPassProbs, double[][] passTime,
            double[][] lambda, double[][] weight, int numEdges) {
        int[] myNodeDegrees = InitHelper.calcNodeDegrees(neighborsSet);

        assertEquals(nodeDegrees.length, myNodeDegrees.length);

        for (int i = 0; i < nodeDegrees.length; i++) {
            assertEquals(nodeDegrees[i], myNodeDegrees[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("values")
    void calcNegPassProbs_shouldReturnCorrectProbs(
            Map<Integer, Set<Integer>> neighborsSet, int[] nodeDegrees,
            double[][] negPassProbs, double[][] passTime,
            double[][] lambda, double[][] weight, int numEdges) {
        int[] myNodeDegrees = InitHelper.calcNodeDegrees(neighborsSet);
        double[][] myNegPassProbs = InitHelper.calcNegPassProbs(neighborsSet, myNodeDegrees);

        assertEquals(negPassProbs.length, myNegPassProbs.length);
        assertEquals(negPassProbs[0].length, myNegPassProbs[0].length);

        for (int i = 0; i < negPassProbs.length; i++) {
            for (int j = 0; j < negPassProbs.length; j++) {
                assertEquals(negPassProbs[i][j], myNegPassProbs[i][j], EPSILON);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("values")
    void calcPassTime_shouldReturnCorrectPassTime(
            Map<Integer, Set<Integer>> neighborsSet, int[] nodeDegrees,
            double[][] negPassProbs, double[][] passTime,
            double[][] lambda, double[][] weight, int numEdges) {
        int[] myNodeDegrees = InitHelper.calcNodeDegrees(neighborsSet);
        double[][] myNegPassProbs = InitHelper.calcNegPassProbs(neighborsSet, myNodeDegrees);
        double[][] myPassTime = InitHelper.calcPassTime(myNegPassProbs);

        for (int i = 0; i < passTime.length; i++) {
            for (int j = 0; j < passTime.length; j++) {
                assertEquals(passTime[i][j], myPassTime[i][j], EPSILON);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("values")
    void calcLambda_shouldReturnCorrectLambda(
            Map<Integer, Set<Integer>> neighborsSet, int[] nodeDegrees,
            double[][] negPassProbs, double[][] passTime,
            double[][] lambda, double[][] weight, int numEdges) {
        int[] myNodeDegrees = InitHelper.calcNodeDegrees(neighborsSet);
        double[][] myNegPassProbs = InitHelper.calcNegPassProbs(neighborsSet, myNodeDegrees);
        double[][] myPassTime = InitHelper.calcPassTime(myNegPassProbs);
        double[][] myLambda = InitHelper.calcLambda(myPassTime);

        for (int i = 0; i < lambda.length; i++) {
            for (int j = 0; j < lambda.length; j++) {
                assertEquals(lambda[i][j], myLambda[i][j], EPSILON);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("values")
    void calcExpWeight_shouldReturnCorrectWeight(
            Map<Integer, Set<Integer>> neighborsSet, int[] nodeDegrees,
            double[][] negPassProbs, double[][] passTime,
            double[][] lambda, double[][] weight, int numEdges) {
        int[] myNodeDegrees = InitHelper.calcNodeDegrees(neighborsSet);
        double[][] myWeight = InitHelper.calcExpWeight(myNodeDegrees, numEdges);

        for (int i = 0; i < weight.length; i++) {
            for (int j = 0; j < weight.length; j++) {
                assertEquals(weight[i][j], myWeight[i][j], EPSILON);
            }
        }
    }
}