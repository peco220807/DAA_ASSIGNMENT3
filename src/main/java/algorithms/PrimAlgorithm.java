package algorithms;

import graph.Graph;
import graph.Edge;
import model.MSTResult;
import java.util.*;

public class PrimAlgorithm {
    public static List<Edge> findMST(Graph graph, long[] operations) {
        List<Edge> mstEdges = new ArrayList<>();
        int numVertices = graph.V();
        if (numVertices == 0) return mstEdges;

        boolean[] inMST = new boolean[numVertices];
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));

        String start = graph.nameOf(0);
        inMST[0] = true;

        Set<String> addedEdges = new HashSet<>();
        for (Edge e : graph.adj(start)) {
            String key = e.getFrom() + "-" + e.getTo();
            if (addedEdges.add(key)) pq.add(e);
            operations[0]++;
        }

        while (!pq.isEmpty() && mstEdges.size() < numVertices - 1) {
            Edge e = pq.poll();
            operations[0]++;

            int toIdx = graph.indexOf(e.getTo());
            int fromIdx = graph.indexOf(e.getFrom());

            if (inMST[toIdx] && inMST[fromIdx]) continue;

            mstEdges.add(e);
            operations[0]++;

            int newIdx = !inMST[toIdx] ? toIdx : fromIdx;
            inMST[newIdx] = true;
            String newVertex = graph.nameOf(newIdx);

            for (Edge next : graph.adj(newVertex)) {
                int idx = graph.indexOf(next.getTo());
                if (!inMST[idx]) {
                    String key = next.getFrom() + "-" + next.getTo();
                    if (addedEdges.add(key)) pq.add(next);
                    operations[0]++;
                }
            }
        }
        return mstEdges;
    }

    public static MSTResult computeMST(Graph graph) {
        long[] operations = {0};
        long start = System.nanoTime();

        List<Edge> mstEdges = findMST(graph, operations);

        long end = System.nanoTime();
        int totalCost = mstEdges.stream().mapToInt(Edge::getWeight).sum();
        long durationNs = end - start;

        return new MSTResult(
                "Prim",
                graph.id(),
                mstEdges,
                totalCost,
                durationNs,
                operations[0]
        );
    }
}

