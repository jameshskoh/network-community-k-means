package commkmeans.util;

import java.util.*;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
public class InitHelper {
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

    // use EJML to calculate t
    public static double[][] calcPassTime(double[][] negPassProbs) {
        int numVert = negPassProbs.length;

        double[][] passTime = new double[numVert][numVert];

        for (int y = 0; y < numVert; y++) {
            DMatrixRMaj matA = calcAbsorbProbs(negPassProbs, y);

            DMatrixRMaj vecX = new DMatrixRMaj(numVert, 1);

            DMatrixRMaj vecI = new DMatrixRMaj(numVert, 1);
            vecI.fill(1.0);

            CommonOps_DDRM.solve(matA, vecI, vecX);

            for (int x = 0; x < numVert; x++) {
                passTime[x][y] = vecX.get(x, 0);
            }
        }

        return passTime;
    }

    private static DMatrixRMaj calcAbsorbProbs(double[][] negPassProbs, int y) {
        int numVert = negPassProbs.length;

        DMatrixRMaj absorbProb = new DMatrixRMaj(negPassProbs);

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
