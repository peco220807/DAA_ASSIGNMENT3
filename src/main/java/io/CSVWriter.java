package io;
import model.MSTResult;
import java.io.FileWriter;
import java.io.IOException;
public class CSVWriter {
    public static void initCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Graph Size,Graph ID,Vertices,Edges,"
                    + "Prim Total Cost,Prim Ops,Prim Time (ns),"
                    + "Kruskal Total Cost,Kruskal Ops,Kruskal Time (ns)\n");
        } catch (IOException e) {
            throw new RuntimeException("Error initializing CSV file", e);
        }
    }
    public static void appendCSV(String filePath, String sizeLabel, String graphId, int vertices, int edges, MSTResult prim, MSTResult kruskal) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(String.format("%s,%s,%d,%d,%.0f,%.0f,%.3f,%.0f,%.0f,%.3f\n",
                    sizeLabel,
                    graphId,
                    vertices,
                    edges,
                    (double) prim.getTotalCost(),
                    (double) prim.getOperationsCount(),
                    prim.getExecutionTimeMs() / 1_000_000.0,
                    (double) kruskal.getTotalCost(),
                    (double) kruskal.getOperationsCount(),
                    kruskal.getExecutionTimeMs() / 1_000_000.0
            ));
        } catch (IOException e) {
            throw new RuntimeException("Error writing to CSV file", e);
        }
    }
}
