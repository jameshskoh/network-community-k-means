package networks;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class EdgeWriter {
    public static void write(
            Map<Integer, Set<Integer>> neighborSets , String fileName, int order) {
        FileWriter fw;

        List<String> lines = new ArrayList<>();

        try {
            fw = new FileWriter(fileName);

            for (int node : neighborSets.keySet()) {
                for (int neighbor : neighborSets.get(node)) {
                    String msg = String.format("%d %d", node, neighbor);
                    lines.add(msg);
                }
            }

            Collections.sort(lines);

            for (String line : lines) {
                fw.write(line);
                fw.write(System.lineSeparator());
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
