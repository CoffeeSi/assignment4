package graph.dagsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import graph.graph.Edge;
import graph.graph.Graph;

public class DAGShortestPath {
    public void shortestPath(Graph graph, List<Integer> topo) {
        if (graph == null) throw new IllegalArgumentException("graph must not be null");
        if (topo == null) throw new IllegalArgumentException("topo order must not be null");

        int n = graph.getN();
        int source = graph.getSource();

        List<List<Edge>> adj = buildAdj(graph);

        long[] dist = new long[n];
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Long.MAX_VALUE;
            prev[i] = -1;
        }
        if (source < 0 || source >= n) throw new IllegalArgumentException("invalid source");
        dist[source] = 0;

        for (int u : topo) {
            if (dist[u] == Long.MAX_VALUE) continue;
            for (Edge edge : adj.get(u)) {
                int v = edge.getV();
                long nd = dist[u] + edge.getW();
                if (nd < dist[v]) {
                    dist[v] = nd;
                    prev[v] = u;
                }
            }
        }

        System.out.println("\nShortest paths:");
        System.out.println("Source: " + source);
        for (int i = 0; i < n; i++) {
            if (dist[i] == Long.MAX_VALUE)
                System.out.println(i + ": infinity");
            else
                System.out.println(i + ": " + dist[i]);
        }

        System.out.println("\nReconstructed paths:");
        for (int i = 0; i < n; i++) {
            if (dist[i] != Long.MAX_VALUE) {
                System.out.println("  Path to " + i + ": " + reconstructPath(prev, i));
            }
        }
    }

    public void longestPath(Graph graph, List<Integer> topo) {
        if (graph == null) throw new IllegalArgumentException("graph must not be null");
        if (topo == null) throw new IllegalArgumentException("topo order must not be null");
        
        int n = graph.getN();
        int source = graph.getSource();

        List<List<Edge>> adj = buildAdj(graph);

        long[] dist = new long[n];
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Long.MIN_VALUE;
            prev[i] = -1;
        }
        dist[source] = 0;

        for (int u : topo) {
            if (dist[u] == Long.MIN_VALUE) continue;
            for (Edge edge : adj.get(u)) {
                int v = edge.getV();
                long nd = dist[u] + edge.getW();
                if (nd > dist[v]) {
                    dist[v] = nd;
                    prev[v] = u;
                }
            }
        }

        // Critical path
        long best = Long.MIN_VALUE;
        int bestV = -1;
        for (int i = 0; i < n; i++) {
            if (dist[i] > best && dist[i] != Long.MIN_VALUE) {
                best = dist[i];
                bestV = i;
            }
        }

        System.out.println("\nLongest Path:");
        System.out.println("Source: " + source);

        if (bestV == -1) {
            System.out.println("No reachable vertices.");
            return;
        }

        List<Integer> criticalPath = reconstructPath(prev, bestV);
        System.out.println("Critical path length: " + best);
        System.out.println("Critical path vertices: " + criticalPath);
    }

    private List<List<Edge>> buildAdj(Graph graph) {
        int n = graph.getN();

        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        List<Edge> edges = graph.getEdges();
        if (edges != null) {
            for (Edge edge : edges) {
                int u = edge.getU();
                int v = edge.getV();
                if (u >= 0 && u < n && v >= 0 && v < n) {
                    adj.get(u).add(edge);
                } else {
                    throw new IllegalArgumentException(
                        "vertices must not be negative and out of range");
                }
            }
        }
        return adj;
    }

    private List<Integer> reconstructPath(int[] prev, int target) {
        List<Integer> path = new ArrayList<>();
        if (prev == null || target < 0 || target >= prev.length) return path;
        for (int cur = target; cur != -1; cur = prev[cur]) {
            path.add(cur);
        }
        Collections.reverse(path);
        return path;
    }
}
