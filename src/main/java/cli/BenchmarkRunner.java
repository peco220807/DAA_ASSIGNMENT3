package cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import graph.Graph;
import graph.Edge;
import io.JSONReader;
import io.CSVWriter;
import algorithms.PrimAlgorithm;
import algorithms.KruskalAlgorithm;
import model.MSTResult;

import java.io.File;
import java.util.*;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        String[] files = {
                "src/main/resources/graphs_small.json",
                "src/main/resources/graphs_medium.json",
                "src/main/resources/graphs_large.json",
                "src/main/resources/graphs_extra_large.json"
        };

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode resultsArray = mapper.createArrayNode();

        CSVWriter.initCSV("src/main/resources/results.csv");

        for (String file : files) {
            Graph graph = JSONReader.loadGraph(file);

            if (!isConnected(graph)) {
                System.out.println("Skipping disconnected graph: " + graph.id());
                continue;
            }

            MSTResult prim = PrimAlgorithm.computeMST(graph);
            MSTResult kruskal = KruskalAlgorithm.run(graph);

            ObjectNode graphNode = mapper.createObjectNode();
            graphNode.put("graph_id", graph.id());

            ObjectNode statsNode = mapper.createObjectNode();
            statsNode.put("vertices", graph.V());
            statsNode.put("edges", graph.E());
            graphNode.set("input_stats", statsNode);

            graphNode.set("prim", makeAlgoNode(mapper, prim));
            graphNode.set("kruskal", makeAlgoNode(mapper, kruskal));

            resultsArray.add(graphNode);

            // Append to CSV file
            String sizeLabel = getSizeLabel(file);
            CSVWriter.appendCSV(
                    "src/main/resources/results.csv",
                    sizeLabel,
                    graph.id(),
                    graph.V(),
                    graph.E(),
                    prim,
                    kruskal
            );
        }

        ObjectNode root = mapper.createObjectNode();
        root.set("results", resultsArray);
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File("src/main/resources/output.json"), root);

        System.out.println("\nResults saved:");
        System.out.println(" - JSON → src/main/resources/output.json");
        System.out.println(" - CSV  → src/main/resources/results.csv");
    }

    private static ObjectNode makeAlgoNode(ObjectMapper mapper, MSTResult result) {
        ObjectNode algoNode = mapper.createObjectNode();

        ArrayNode edgesArray = mapper.createArrayNode();
        for (Edge e : result.getEdges()) {
            ObjectNode edgeNode = mapper.createObjectNode();
            edgeNode.put("from", e.getFrom());
            edgeNode.put("to", e.getTo());
            edgeNode.put("weight", e.getWeight());
            edgesArray.add(edgeNode);
        }

        algoNode.set("mst_edges", edgesArray);
        algoNode.put("total_cost", result.getTotalCost());
        algoNode.put("operations_count", result.getOperationsCount());
        algoNode.put("execution_time_ns", result.getExecutionTimeMs() / 1_000_000.0);

        return algoNode;
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

    private static String getSizeLabel(String filePath) {
        if (filePath.contains("small")) return "SMALL";
        if (filePath.contains("medium")) return "MEDIUM";
        if (filePath.contains("large")) return "LARGE";
        if (filePath.contains("extra_large")) return "EXTRA_LARGE";
        return "UNKNOWN";
    }
}
