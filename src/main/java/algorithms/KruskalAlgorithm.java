package algorithms;
import graph.Edge;
import graph.Graph;
import graph.MSTResult;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class KruskalAlgorithm {
    public MSTResult run(Graph g) {
        long t0 = System.nanoTime();
        List<Edge> allEdges = new ArrayList<>(g.edges());
        allEdges.sort(Comparator.comparingInt(e -> e.weight));
        DisjointSet dsu = new DisjointSet(g.V());
        List<Edge> mst = new ArrayList<>();
        int total = 0;
        for (Edge e : allEdges) {
            int u = g.indexOf(e.to);
            int v = g.indexOf(e.to);
            if (dsu.union(u, v)) {
                mst.add(e);
                total += e.weight;
                if (mst.size() == g.V() - 1) break;
            }
        }
        long ms = (System.nanoTime() - t0) / 1_000_000;
        return new MSTResult(mst, total, g.V(), g.E(), ms, "Kruskal", g.id());
    }
}