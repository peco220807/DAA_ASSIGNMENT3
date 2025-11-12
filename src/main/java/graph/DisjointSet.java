package graph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
public class DisjointSet {
    private final Map<String, String> parent;
    private final Map<String, Integer> rank;
    public DisjointSet() {
        parent = new HashMap<>();
        rank = new HashMap<>();
    }
    public void add(String element) {
        parent.putIfAbsent(element, element);
        rank.putIfAbsent(element, 0);
    }
    public String find(String element) {
        if (!parent.get(element).equals(element)) {
            parent.put(element, find(parent.get(element))); // Сжимаем путь
        }
        return parent.get(element);
    }
    public void union(String element1, String element2) {
        String root1 = find(element1);
        String root2 = find(element2);

        if (!root1.equals(root2)) {
            if (rank.get(root1) > rank.get(root2)) {
                parent.put(root2, root1);
            } else if (rank.get(root1) < rank.get(root2)) {
                parent.put(root1, root2);
            } else {
                parent.put(root2, root1);
                rank.put(root1, rank.get(root1) + 1);
            }
        }
    }
    public List<List<String>> getComponents(List<Edge> mst, int n) {
        Map<String, List<String>> components = new HashMap<>();
        for (Edge edge : mst) {
            String rootU = find(edge.getFrom());
            String rootV = find(edge.getTo());
            components.putIfAbsent(rootU, new ArrayList<>());
            components.putIfAbsent(rootV, new ArrayList<>());
            components.get(rootU).add(edge.getFrom());
            components.get(rootV).add(edge.getTo());
        }
        return new ArrayList<>(components.values());
    }
}