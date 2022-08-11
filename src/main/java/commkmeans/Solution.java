package commkmeans;

import java.util.Map;
import java.util.Set;

public class Solution {
    public double optObj;
    public Map<Integer, Set<Integer>> optCommSets;

    public void printSolution() {
        System.out.println("Solution:\n");

        String msg = String.format("Optimal objective: %f", optObj);
        System.out.println(msg);

        System.out.println("\n");

        int commCount = 1;
        for (int center : optCommSets.keySet()) {
            String msg1 = String.format("Comm #%d \n", commCount);
            System.out.println(msg1);

            String msg2 = String.format("Center node: %d", center);
            String msg3 = "Containing nodes:";
            System.out.println(msg2);
            System.out.println(msg3);

            int count = 1;
            for (int node : optCommSets.get(center)) {
                String msg4 = String.format("%d\t", node);
                System.out.print(msg4);

                if (count % 6 == 0) System.out.println();

                count++;
            }

            System.out.println();

            commCount++;
        }
    }
}
