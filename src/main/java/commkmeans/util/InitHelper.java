package commkmeans.util;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class InitHelper {
    public static double[] vecSingular;

    public static int[] calcNodeDegrees(Map<Integer, Set<Integer>> neighborsSet) {
        int[] nodeDegrees = new int[neighborsSet.size()];

        for (int node : neighborsSet.keySet()) {

            Set<Integer> neighbors = neighborsSet.get(node);

            if (neighbors != null && !neighbors.isEmpty()) {
                nodeDegrees[node] = neighborsSet.get(node).size();
            }
        }

        return nodeDegrees;
    }

    public static double[][] calcNegPassProbs(Map<Integer, Set<Integer>> neighborsSet, int[] nodeDegrees) {
        int numVert = neighborsSet.size();

        double[][] negPassProbs = new double[numVert][numVert];

        for (int i = 0; i < numVert; i++) {
            for (int j = 0; j < numVert; j++) {
                if (neighborsSet.containsKey(i) && neighborsSet.get(i).contains(j)) {
                    negPassProbs[i][j] = -1.0 / nodeDegrees[i];
                }
            }
        }

        return negPassProbs;
    }

    // Solve system of equations with MTJ
    public static double[][] calcPassTime(double[][] negPassProbs) {
        int numVert = negPassProbs.length;

        double[][] passTime = new double[numVert][numVert];

        IntStream dests = IntStream.range(0, numVert);

        dests.parallel().forEach(y -> {
            String msg = String.format("Running instance %d.", y);
            System.out.println(msg);

            Matrix matA = calcAbsorbProbs(negPassProbs, y);
            Vector vecX = new DenseVector(numVert);
            Vector vecI = new DenseVector(vecSingular);

            matA.solve(vecI, vecX);

            for (int x = 0; x < numVert; x++) {
                double value = vecX.get(x);
                // if (value < 1.0) System.out.println(value);

                if (Double.isNaN(value)) System.out.println("OMG " + x + ", " + y + " HIT NAN!");

                if (value == Double.POSITIVE_INFINITY || value == Double.NEGATIVE_INFINITY)
                    System.out.println("OMG " + x + ", " + y + " INFINITY!");

                passTime[x][y] = vecX.get(x);
            }
        });

        return passTime;
    }

    private static DenseMatrix calcAbsorbProbs(double[][] negPassProbs, int y) {
        int numVert = negPassProbs.length;

        DenseMatrix absorbProb = new DenseMatrix(negPassProbs);

        // set column y to 0
        for (int i = 0; i < numVert; i++) {
            absorbProb.set(i, y, 0.0);
        }

        // adds I
        for (int i = 0; i < numVert; i++) {
            absorbProb.add(i, i, 1.0);
        }

        return absorbProb;
    }

    // calculate lambda given set information
    public static double[][] calcLambda(double[][] passTime) {
        int numVert = passTime.length;

        double[][] lambda = new double[numVert][numVert];

        for (int x = 0; x < numVert; x++) {
            for (int y = 0; y < numVert; y++) {
                lambda[x][y] = calcLambdaIter(x, y, passTime, numVert);
            }
        }

        return lambda;
    }

    private static double calcLambdaIter(
            int x, int y, double[][] passTime, int numVert) {
        double sum = 0.0;

        for (int z = 0; z < numVert; z++) {
            if (z == x || z == y) continue;

            double diff = passTime[x][z] - passTime[y][z];
            sum += diff * diff;
        }

        return (Math.sqrt(sum) / (numVert - 2));
    }

    public static double[][] calcExpWeight(int[] nodeDegrees, int numEdges) {
        int numVert = nodeDegrees.length;

        double[][] expWeight = new double[numVert][numVert];

        for (int i = 0; i < numVert; i++) {
            for (int j = 0; j < numVert; j++) {
                expWeight[i][j] = (double)nodeDegrees[i] * nodeDegrees[j] / (2 * numEdges);
            }
        }

        return expWeight;
    }
}
