package org.example;
import graph.Edge;
import graph.Graph;
import algorithms.KruskalAlgorithm;
import model.MSTResult;
import java.util.Arrays;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        try {
            List<String> vertices = Arrays.asList("A", "B", "C", "D", "E");

            List<Edge> edges = Arrays.asList(
                    new Edge("A", "B", 4),
                    new Edge("A", "C", 3),
                    new Edge("B", "C", 1),
                    new Edge("B", "D", 2),
                    new Edge("C", "D", 4),
                    new Edge("C", "E", 5),
                    new Edge("D", "E", 6)
            );

            Graph graph = new Graph("TestGraph", false, vertices, edges);

            System.out.println("Running Kruskal's Algorithm...");
            MSTResult result = KruskalAlgorithm.run(graph);
            System.out.println("MST edges: ");
            for (Edge edge : result.getEdges()) {
                System.out.println(edge);
            }
            System.out.println("Total Cost: " + result.getTotalCost());
            System.out.println("Algorithm Duration: " + result.getExecutionTimeMs() + " ms");
            Edge edgeToRemove = result.getEdges().get(0);
            System.out.println("Removing edge: " + edgeToRemove);
            MSTResult updatedResult = KruskalAlgorithm.updateMSTAfterEdgeRemoval(graph, result.getEdges(), edgeToRemove);
            System.out.println("Updated MST after removing edge " + edgeToRemove + ":");
            for (Edge edge : updatedResult.getEdges()) {
                System.out.println(edge);
            }
            System.out.println("Updated Total Cost: " + updatedResult.getTotalCost());
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
