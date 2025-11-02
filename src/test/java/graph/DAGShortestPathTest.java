package graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import graph.dagsp.DAGShortestPath;
import graph.graph.Edge;
import graph.graph.Graph;
import graph.scc.Kosaraju;
import graph.topo.Kahn;
import metrics.Metrics;

public class DAGShortestPathTest {

    @Test
    public void testSevenNodeExample() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 2));
        edges.add(new Edge(1, 2, 8));
        edges.add(new Edge(2, 3, 3));
        edges.add(new Edge(3, 1, 5));
        edges.add(new Edge(3, 4, 4));
        edges.add(new Edge(4, 5, 7));
        edges.add(new Edge(5, 6, 6));
        Graph g = new Graph(true, 7, edges, 4, "edge");

        Kosaraju scc = new Kosaraju();
        List<List<Integer>> components = scc.kosaraju(g, null);
        assertNotNull(components);
        assertTrue(components.size() > 1);

        Graph condensation = scc.buildCondensation(g, components);
        assertTrue(condensation.isDirected());
        assertTrue(condensation.getN() <= g.getN());

        Kahn kahn = new Kahn();
        List<Integer> topoOrder = kahn.kahn(condensation, null);
        assertNotNull(topoOrder);
        assertFalse(topoOrder.isEmpty());
        assertEquals(condensation.getN(), topoOrder.size());

        DAGShortestPath dagsp = new DAGShortestPath();
        Metrics m = new Metrics("test");

        dagsp.shortestPath(condensation, topoOrder, m);
        dagsp.longestPath(condensation, topoOrder, m);

        assertTrue(m.getTimeMs("dagShortestPath") >= 0);
        assertTrue(m.getTimeMs("dagLongestPath") >= 0);
    }

    @Test
    public void testSimpleDAG_withNegativeWeights() {
        // DAG: 0 -> 1 (-5), 1 -> 2 (3)
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, -5));
        edges.add(new Edge(1, 2, 3));
        Graph g = new Graph(true, 3, edges, 0, "edge");

        Kosaraju scc = new Kosaraju();
        List<List<Integer>> comps = scc.kosaraju(g, null);
        Graph cond = scc.buildCondensation(g, comps);

        Kahn kahn = new Kahn();
        List<Integer> topo = kahn.kahn(cond, null);

        DAGShortestPath dsp = new DAGShortestPath();
        dsp.shortestPath(cond, topo, null);
        dsp.longestPath(cond, topo, null);
    }

    @Test
    public void testSingleNodeGraph() {
        List<Edge> edges = new ArrayList<>();
        Graph g = new Graph(true, 1, edges, 0, "edge");

        Kosaraju scc = new Kosaraju();
        List<List<Integer>> comps = scc.kosaraju(g, null);
        Graph cond = scc.buildCondensation(g, comps);

        Kahn kahn = new Kahn();
        List<Integer> topo = kahn.kahn(cond, null);

        DAGShortestPath dsp = new DAGShortestPath();
        dsp.shortestPath(cond, topo, null);
        dsp.longestPath(cond, topo, null);

        // No edges — should not crash
        assertEquals(1, cond.getN());
        assertEquals(1, topo.size());
    }
}
