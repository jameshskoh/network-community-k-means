package networks;

import commkmeans.graph.Graph;
import commkmeans.util.GraphLoader;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        Graph g = new Graph();

        String inputFileName = "data/facebook_ucsd.edge";
        String outputFileName = "src/main/resources/data/facebook_ucsd_main.edge";

        try {
            GraphLoader.loadGraph(g, inputFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Map<Integer, Set<Integer>> neighborSets = g.exportGraph();

        List<Set<Integer>> subnets = SubnetFinder.findSubnets(neighborSets);
        SubnetFinder.sortSubnetByDecreasingSize(subnets);

        for (int i = 0; i < subnets.size(); i++) {
            String msg = String.format("Set:\t%d Size:\t%d", i, subnets.get(i).size());
            System.out.println(msg);
        }

        System.out.println("Picking the first subset");

        Map<Integer, Set<Integer>> subsetNeighborSets = SubnetFinder.findSubnetNeighborSets(
                neighborSets, subnets, 0);

        Map<Integer, Set<Integer>> relabeledSubsetNeighbotSets = NodeRelabeler.relabel(subsetNeighborSets);

        EdgeWriter.write(relabeledSubsetNeighbotSets, outputFileName, 0);
    }
}
