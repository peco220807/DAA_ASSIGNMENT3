package algorithms;
import graph.Edge;
import graph.DisjointSet;
import graph.Graph;
import model.MSTResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class KruskalAlgorithm {
    public static MSTResult run(Graph graph) {
        long startTime = System.nanoTime();
        long operations = 0;
        List<Edge> edges = new ArrayList<>(graph.edges());
        Collections.sort(edges);
        operations += edges.size();
        DisjointSet uf = new DisjointSet();
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;
        for (String v : graph.getVertices()) {
            uf.add(v);
            operations++;
        }
        for (Edge e : edges) {
            operations++;
            String from = e.getFrom();
            String to = e.getTo();
            if (!uf.find(from).equals(uf.find(to))) {
                uf.union(from, to);
                mstEdges.add(e);
                totalCost += e.getWeight();
            }
            if (mstEdges.size() == graph.V() - 1) break;
        }
        long endTime = System.nanoTime();
        long durationNs = endTime - startTime;
        return new MSTResult(
                "Kruskal",
                graph.id(),
                mstEdges,
                totalCost,
                durationNs,
                operations
        );
    }
}
