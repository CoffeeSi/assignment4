package graph.topo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import graph.graph.Edge;
import graph.graph.Graph;

public class Kahn {
    public List<Integer> kahn(Graph graph) {
        int n = graph.getN();

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++)
            adj.add(new ArrayList<>());

        List<Edge> edges = graph.getEdges();
        if (edges != null) {
            for (Edge edge : edges) {
                int u = edge.getU();
                int v = edge.getV();
                if (u >= 0 && u < n && v >= 0 && v < n) {
                    adj.get(u).add(v);
                } else {
                    throw new IllegalArgumentException(
                        "vertices must not be negative and out of range");
                }
            }
        }

        int[] indegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) {
                indegree[v]++;
            }
        }

        Queue<Integer> q = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                q.add(i);
            }
        }

        List<Integer> valOrder = new ArrayList<>();
        while (!q.isEmpty()) {
            int node = q.poll();
            valOrder.add(node);

            for (int v : adj.get(node)) {
                indegree[v]--;
                if (indegree[v] == 0) {
                    q.add(v);
                }
            }
        }

        boolean isDag = (valOrder.size() == n);
        System.out.println("DAG: " + isDag);
        System.out.println("Topological order: " + valOrder);
        
        return valOrder;
    }

    public void printTopo(Graph graph, List<List<Integer>> sccs) {
        List<Integer> valOrder = kahn(graph);

        List<Integer> derivOrder = new ArrayList<>();
        for (int comp : valOrder) {
            List<Integer> scc = new ArrayList<>(sccs.get(comp));
            Collections.sort(scc);
            derivOrder.addAll(scc);
        }

        System.out.println("Derived order: " + derivOrder);
    }
}
