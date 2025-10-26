package tests;

import algorithms.KruskalAlgorithm;
import algorithms.PrimAlgorithm;
import graph.*;
import io.JSONReader;
import model.MSTResult;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CorrectnessTest {

    @Test
    public void testMSTCorrectnessForAllGraphs() {
        String[] files = {
                "src/main/resources/datasets/graphs_small.json",
                "src/main/resources/datasets/graphs_medium.json",
                "src/main/resources/datasets/graphs_large.json",
                "src/main/resources/datasets/graphs_extra_large.json"
        };

        for (String file : files) {
            List<Graph> graphs = JSONReader.loadGraphs(file);
            if (graphs == null || graphs.isEmpty()) {
                System.err.println("Warning: no graphs loaded from file " + file);
                continue;
            }

            for (Graph graph : graphs) {
                if (graph == null || graph.V() == 0) continue;

                boolean skipValidation = graph.V() > 1000;

                if (!isConnected(graph)) {
                    System.out.println("Skipping disconnected graph: " + graph.id());
                    continue;
                }

                MSTResult kruskal = KruskalAlgorithm.run(graph);
                MSTResult prim = new PrimAlgorithm().computeMST(graph);

                if (!skipValidation) {
                    validateMST(graph, kruskal);
                    validateMST(graph, prim);

                    assertEquals(kruskal.getTotalCost(), prim.getTotalCost(),
                            "MST total cost must be identical for graph " + graph.id());
                }

                System.out.println("Graph " + graph.id() + ": Kruskal " + kruskal.getExecutionTimeMs() + "ms, Prim " + prim.getExecutionTimeMs() + "ms");
            }
        }
    }

    private void validateMST(Graph graph, MSTResult mst) {
        List<Edge> edges = mst.getEdges();
        List<String> vertices = graph.getVertices();

        assertEquals(vertices.size() - 1, edges.size(), "MST must have V-1 edges");

        DisjointSet uf = new DisjointSet();
        for (String v : vertices) uf.add(v);

        for (Edge e : edges) {
            String r1 = uf.find(e.getFrom());
            String r2 = uf.find(e.getTo());
            assertNotEquals(r1, r2, "MST must be acyclic");
            uf.union(e.getFrom(), e.getTo());
        }

        String root = uf.find(vertices.get(0));
        for (String v : vertices) {
            assertEquals(root, uf.find(v), "MST must connect all vertices");
        }

        assertTrue(mst.getExecutionTimeMs() >= 0, "ExecutionTimeMs must be non-negative");
        assertTrue(mst.getOperationsCount() >= 0, "OperationsCount must be non-negative");
    }

    private boolean isConnected(Graph graph) {
        if (graph.V() == 0) return true;

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        String start = graph.getVertices().get(0);
        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            String v = queue.poll();
            for (Edge e : graph.adj(v)) {
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
