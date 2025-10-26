package tests;
import algorithms.KruskalAlgorithm;
import algorithms.PrimAlgorithm;
import graph.Graph;
import io.JSONReader;
import model.MSTResult;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PerformanceTest {
    @Test
    public void testPerformanceOfAlgorithms() {
        String[] files = {
                "src/main/resources/graphs_small.json",
                "src/main/resources/graphs_medium.json",
                "src/main/resources/graphs_large.json",
                "src/main/resources/graphs_extra_large.json"
        };

        for (String file : files) {
            List<Graph> graphs = JSONReader.loadGraphs(file);
            if (graphs == null || graphs.isEmpty()) continue;

            for (Graph graph : graphs) {
                if (graph == null || graph.V() == 0) continue;
                System.out.println("Graph " + graph.id() + " | V=" + graph.V() + ", E=" + graph.E());
                long startK = System.currentTimeMillis();
                MSTResult kruskal = KruskalAlgorithm.run(graph);
                long endK = System.currentTimeMillis();

                long startP = System.currentTimeMillis();
                MSTResult prim = new PrimAlgorithm().computeMST(graph);
                long endP = System.currentTimeMillis();

                System.out.println("Kruskal: " + (endK - startK) + "ms | Prim: " + (endP - startP) + "ms");
                System.out.println("MST total cost: Kruskal=" + kruskal.getTotalCost() + ", Prim=" + prim.getTotalCost());
            }
        }
    }
}
