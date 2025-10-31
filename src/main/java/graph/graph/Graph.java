package graph.graph;

import java.util.List;

public class Graph {
    private boolean directed;
    private int n;
    private List<Edge> edges;
    private int source;
    private String weight_model;

    public Graph(boolean directed,
            int n,
            List<Edge> edges,
            int source,
            String weight_model) {
        this.directed = directed;
        this.n = n;
        this.edges = edges;
        this.source = source;
        this.weight_model = weight_model;
    }

    public boolean isDirected() { return directed; }
    public int getN() { return n;}
    public List<Edge> getEdges() { return edges; }
    public int getSource() { return source; }
    public String getModel() { return weight_model; }

    public void addEdge(int u, int v, int w) {
        edges.add(new Edge(u, v, w));
    }

    public String toString() {
        return "directed: " + directed + 
                "\nn: " + n +
                "\nedges" + edges +
                "\nsource: " + source +
                "\nweight_model" + weight_model;
    }
}
