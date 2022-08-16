package commkmeans;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Solution {
    public double optObj;
    public Map<Integer, Set<Integer>> optCommSets;
    public double[] objHistogram;
    public long initTime;
    public long finishTime;

    public Solution(int numIter) {
        objHistogram = new double[numIter];
    }

    public void printSolution() {
        System.out.println("Solution:\n");

        String msg = String.format("Best solution: %f", -optObj);
        System.out.println(msg);

        System.out.println("\n");

        int commCount = 1;
        for (int center : optCommSets.keySet()) {
            String msg1 = String.format("\nComm #%d \n", commCount);
            System.out.println(msg1);

            String msg2 = String.format("Center node: %d", center);
            String msg3 = "Containing nodes:";
            System.out.println(msg2);
            System.out.println(msg3);

            int count = 1;
            for (int node : optCommSets.get(center)) {
                if (node < 1000) {
                    String msg4 = String.format("%d\t\t", node);
                    System.out.print(msg4);
                } else {
                    String msg4 = String.format("%d\t", node);
                    System.out.print(msg4);
                }

                if (count % 12 == 0) System.out.println();

                count++;
            }

            System.out.println();

            commCount++;
        }

        System.out.println(msg);
    }

    public void exportSolution(String fileName, long initTime, long finishTime) throws IOException {
        FileWriter fw;

        fw = new FileWriter(fileName);

        String msg;

        msg = String.format("Initialization time %.4f s", initTime / 1.0E9);
        fw.write(msg);
        fw.write(System.lineSeparator());

        msg = String.format("Computation time %.4f s", finishTime / 1.0E9);
        fw.write(msg);
        fw.write(System.lineSeparator());

        msg = String.format("Best solution: %.6f", -optObj);
        fw.write(msg);
        fw.write(System.lineSeparator());
        fw.write(System.lineSeparator());

        int commIndex = 1;
        for (int center : optCommSets.keySet()) {
            msg = String.format("Comm #%d\nCenter #%d", commIndex, center);
            fw.write(msg);
            fw.write(System.lineSeparator());

            int nodeCount = 1;
            for (int node : optCommSets.get(center)) {
                msg = String.format("%d\t", node);
                fw.write(msg);

                if (nodeCount % 12 == 0) fw.write(System.lineSeparator());

                nodeCount++;
            }

            fw.write(System.lineSeparator());
            fw.write(System.lineSeparator());

            commIndex++;
        }

        fw.write("Solution histogram: ");
        fw.write(System.lineSeparator());

        int i = 0;
        while (i < objHistogram.length && objHistogram[i] != 0.0) {
            msg = String.format("%.5f\t", -objHistogram[i]);
            fw.write(msg);
            fw.write(System.lineSeparator());
            i++;
        }

        fw.close();
    }
}
