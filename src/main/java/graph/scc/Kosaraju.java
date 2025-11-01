package graph.scc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graph.graph.Edge;
import graph.graph.Graph;

public class Kosaraju {
    public List<List<Integer>> kosaraju(Graph graph) {
        if (graph == null) throw new IllegalArgumentException("graph must not be null");
        
        List<List<Integer>> adj = new ArrayList<>(graph.getN());
        List<List<Integer>> rev = new ArrayList<>(graph.getN());

        for (int i = 0; i < graph.getN(); i++) {
            adj.add(new ArrayList<>());
            rev.add(new ArrayList<>());
        }

        for (Edge e : graph.getEdges()) {
            adj.get(e.getU()).add(e.getV());
            rev.get(e.getV()).add(e.getU());
        }

        boolean[] visited = new boolean[graph.getN()];
        Deque<Integer> order = new ArrayDeque<>();
        for (int i = 0; i < graph.getN(); i++) {
            if (!visited[i])
                dfs1(i, adj, visited, order);
        }
        visited = new boolean[graph.getN()];
        List<List<Integer>> components = new ArrayList<>();

        while (!order.isEmpty()) {
            int v = order.removeLast();
            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                dfs2(v, rev, visited, component);
                Collections.sort(component);
                components.add(component);
            }
        }
        return components;
    }

    private void dfs1(int v, List<List<Integer>> adj, boolean[] visited, Deque<Integer> order) {
        visited[v] = true;
        for (int to : adj.get(v)) {
            if (!visited[to])
                dfs1(to, adj, visited, order);
        }
        order.addLast(v);
    }

    private void dfs2(int v, List<List<Integer>> rev, boolean[] visited, List<Integer> component) {
        visited[v] = true;
        component.add(v);
        for (int to : rev.get(v)) {
            if (!visited[to])
                dfs2(to, rev, visited, component);
        }
    }

    public Graph buildCondensation(Graph g, List<List<Integer>> sccs) {
        if (g == null) throw new IllegalArgumentException("graph must not be null");
        if (sccs == null) throw new IllegalArgumentException("sccs must not be null");

        int n = g.getN();
        int k = sccs.size();

        int[] comp = new int[n];
        for (int i = 0; i < k; i++) {
            for (int v : sccs.get(i)) {
                comp[v] = i;
            }
        }

        Set<String> seen = new HashSet<>();
        List<Edge> edges = new ArrayList<>();

        if (g.getEdges() != null) {
            for (Edge e : g.getEdges()) {
                int cu = comp[e.getU()];
                int cv = comp[e.getV()];
                if (cu != cv) {
                    String key = cu + "-" + cv;
                    if (seen.add(key)) {
                        edges.add(new Edge(cu, cv, 1));
                    }
                }
            }
        }

        int sourceComp = comp[g.getSource()];
        return new Graph(true, k, edges, sourceComp, "condensation");
    }

    public void printSCC(List<List<Integer>> sccList) {
        System.out.println("Strongly connected components:");
        for (int i = 0; i < sccList.size(); i++) {
            List<Integer> component = new ArrayList<>(sccList.get(i));
            System.out.println(i+1 + ": " + component + " size: " + component.size());
        }
    }
}
