package org.example.benchmark;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.algorithms.Kruskal;
import org.example.algorithms.Prim;
import org.example.graph.Graph;
import org.example.model.MSTOutput;
import org.example.model.MSTResult;
import org.example.io.GraphLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        String[] files = {
                "src/main/resources/datasets/graphs_small.json",
                "src/main/resources/datasets/graphs_medium.json",
                "src/main/resources/datasets/graphs_large.json",
                "src/main/resources/datasets/graphs_extra_large.json"
        };
        List<MSTOutput> outputs = new ArrayList<>();
        Prim primAlg = new Prim();
        for (String file : files) {
            List<Graph> graphs = GraphLoader.loadGraphs(file);

            for (Graph g : graphs) {
                if (!isConnected(g)) {
                    System.out.println("Skipping disconnected graph: " + g.id());
                    continue;
                }
                MSTResult prim = primAlg.computeMST(g);
                MSTResult kruskal = Kruskal.run(g);
                MSTOutput.InputStats stats = new MSTOutput.InputStats(g.V(), g.E());
                MSTOutput.MSTData primData = new MSTOutput.MSTData(
                        prim.getEdges(),
                        prim.getTotalCost(),
                        prim.getOperationsCount(),
                        prim.getExecutionTimeMs()
                );
                MSTOutput.MSTData kruskalData = new MSTOutput.MSTData(
                        kruskal.getEdges(),
                        kruskal.getTotalCost(),
                        kruskal.getOperationsCount(),
                        kruskal.getExecutionTimeMs()
                );

                outputs.add(new MSTOutput(g.id(), stats, primData, kruskalData));
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File("src/main/resources/datasets/output.json"),
                        java.util.Map.of("results", outputs));

        System.out.println("All MST results saved to output.json");
    }
    private static boolean isConnected(Graph graph) {
        if (graph.V() == 0) return true;
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        String start = graph.getVertices().get(0);
        visited.add(start);
        queue.add(start);
        while (!queue.isEmpty()) {
            String v = queue.poll();
            for (var e : graph.adj(v)) {
                String u = e.getTo();
                if (!visited.contains(u)) {
                    visited.add(u);
                    queue.add(u);
                }
            }
        }
        return visited.size() == graph.V();
    }
}