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
        return new MSTResult("Kruskal", graph.id(), mstEdges, totalCost, durationNs, operations);
    }
    public static List<Edge> removeEdgeFromMST(List<Edge> mst, Edge edgeToRemove) {
        List<Edge> mutableMST = new ArrayList<>(mst);
        mutableMST.remove(edgeToRemove);
        return mutableMST;
    }
    public static MSTResult updateMSTAfterEdgeRemoval(Graph graph, List<Edge> mst, Edge edgeToRemove) {
        long startTime = System.nanoTime();
        mst = removeEdgeFromMST(mst, edgeToRemove);
        DisjointSet uf = new DisjointSet();
        for (String v : graph.getVertices()) {
            uf.add(v);
        }
        for (Edge edge : mst) {
            uf.union(edge.getFrom(), edge.getTo());
        }
        List<List<String>> components = uf.getComponents(mst, graph.V());
        Edge replacementEdge = findReplacementEdge(graph.edges(), components);
        if (replacementEdge != null) {
            mst.add(replacementEdge);
        }
        int totalCost = 0;
        for (Edge e : mst) {
            totalCost += e.getWeight();
        }
        long durationNs = System.nanoTime() - startTime;
        return new MSTResult("Kruskal", graph.id(), mst, totalCost, durationNs, mst.size());
    }
    public static Edge findReplacementEdge(List<Edge> edges, List<List<String>> components) {
        int minWeight = Integer.MAX_VALUE;
        Edge replacementEdge = null;
        for (Edge edge : edges) {
            for (List<String> component1 : components) {
                for (List<String> component2 : components) {
                    if (component1 != component2 && component1.contains(edge.getFrom()) && component2.contains(edge.getTo())) {
                        if (edge.getWeight() < minWeight) {
                            minWeight = edge.getWeight();
                            replacementEdge = edge;
                        }
                    }
                }
            }
        }
        return replacementEdge;
    }
}
