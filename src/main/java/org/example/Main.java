package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import graph.Graph;
import algorithms.KruskalAlgorithm;
import algorithms.PrimAlgorithm;
import io.JSONReader;
import io.JSONWriter;

import model.MSTResult;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== MST Analysis Start =====");

        String[] graphFiles = {
                "src/main/resources/graphs_small.json",
                "src/main/resources/graphs_medium.json",
                "src/main/resources/graphs_large.json",
                "src/main/resources/graphs_extra_large.json",
        };

        for (String file : graphFiles) {
            System.out.println("\nLoading graph: " + file);
            Graph graph = JSONReader.loadGraph(file);

            if (graph == null) {
                System.out.println("Failed to load file: " + file);
                continue;
            }

            MSTResult resultKruskal = KruskalAlgorithm.run(graph);
            System.out.println("Kruskal result -> Cost: " + resultKruskal.getTotalCost() +
                    " | Time: " + resultKruskal.getExecutionTimeMs() + " nano sec" +
                    " | Ops: " + resultKruskal.getOperationsCount());
            JSONWriter.writeToJson("results/Kruskal_" + graph.id() + ".json", resultKruskal);

            MSTResult resultPrim = PrimAlgorithm.computeMST(graph);
            System.out.println("Prim result -> Cost: " + resultPrim.getTotalCost() +
                    " | Time: " + resultPrim.getExecutionTimeMs() + " nano sec" +
                    " | Ops: " + resultPrim.getOperationsCount());
            JSONWriter.writeToJson("results/Prim_" + graph.id() + ".json", resultPrim);
        }

        System.out.println("\nMST Analysis Complete");
    }
}
