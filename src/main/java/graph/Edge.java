package graph;
public class Edge {
    public final String from;  // Начальная вершина
    public final String to;    // Конечная вершина
    public final int weight;   // Вес рёбер
    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "(" + from + " - " + to + ", w=" + weight + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge e = (Edge) o;

        return weight == e.weight &&
                ((from.equals(e.from) && to.equals(e.to)) ||
                        (from.equals(e.to) && to.equals(e.from)));
    }

    @Override
    public int hashCode() {
        // Сортировка вершин по алфавиту для унификации хеш-кода
        String first = from.compareTo(to) < 0 ? from : to;
        String second = from.compareTo(to) < 0 ? to : from;
        return first.hashCode() + second.hashCode() + Integer.hashCode(weight);
    }
}