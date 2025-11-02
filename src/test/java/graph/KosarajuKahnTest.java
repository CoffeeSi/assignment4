package graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import graph.graph.Edge;
import graph.graph.Graph;
import graph.scc.Kosaraju;
import graph.topo.Kahn;

public class KosarajuKahnTest {
    @Test
    public void singleNode_noEdges() {
        List<Edge> edges = new ArrayList<>();
        Graph g = new Graph(true, 1, edges, 0, "edge");

        Kosaraju scc = new Kosaraju();
        List<List<Integer>> comps = scc.kosaraju(g, null);

        assertEquals(1, comps.size());
        assertEquals(1, comps.get(0).size());
        assertTrue(comps.get(0).contains(0));

        Kahn k = new Kahn();
        Graph cond = scc.buildCondensation(g, comps);
        List<Integer> topo = k.kahn(cond, null);
        assertEquals(cond.getN(), topo.size());
        assertEquals(0, topo.get(0).intValue());
    }

    @Test
    public void testSimpleCycle() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 1));
        edges.add(new Edge(1, 2, 1));
        edges.add(new Edge(2, 0, 1));
        Graph g = new Graph(true, 3, edges, 0, "edge");

        Kosaraju scc = new Kosaraju();
        List<List<Integer>> comps = scc.kosaraju(g, null);

        assertEquals(1, comps.size());
        List<Integer> comp = comps.get(0);
        assertTrue(comp.contains(0) && comp.contains(1) && comp.contains(2));

        Graph cond = scc.buildCondensation(g, comps);
        assertEquals(1, cond.getN());
        Kahn k = new Kahn();
        List<Integer> topo = k.kahn(cond, null);
        assertEquals(1, topo.size());
        assertEquals(0, topo.get(0).intValue());
    }

    @Test
    public void disconnectedNodes_eachIsComponent() {
        List<Edge> edges = new ArrayList<>(); // no edges
        Graph g = new Graph(true, 4, edges, 0, "edge");

        Kosaraju scc = new Kosaraju();
        List<List<Integer>> comps = scc.kosaraju(g, null);

        assertEquals(4, comps.size());

        Graph cond = scc.buildCondensation(g, comps);
        assertEquals(4, cond.getN());

        Kahn k = new Kahn();
        List<Integer> topo = k.kahn(cond, null);
        assertEquals(4, topo.size());
        assertTrue(topo.contains(0) && topo.contains(1) && topo.contains(2) && topo.contains(3));
    }

    @Test
    public void smallDAG_topoOrderProperty() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0,1,1));
        edges.add(new Edge(0,2,1));
        edges.add(new Edge(1,3,1));
        edges.add(new Edge(2,3,1));
        Graph g = new Graph(true, 4, edges, 0, "edge");

        Kahn k = new Kahn();
        List<Integer> topo = k.kahn(g, null);
        assertEquals(4, topo.size());
        assertTrue(topo.indexOf(0) < topo.indexOf(1));
        assertTrue(topo.indexOf(0) < topo.indexOf(2));
        assertTrue(topo.indexOf(1) < topo.indexOf(3));
        assertTrue(topo.indexOf(2) < topo.indexOf(3));
    }

    @Test
    public void kahnDetectsCycle() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0,1,1));
        edges.add(new Edge(1,2,1));
        edges.add(new Edge(2,0,1));
        Graph g = new Graph(true, 3, edges, 0, "edge");

        Kahn k = new Kahn();
        List<Integer> topo = k.kahn(g, null);
        assertTrue(topo.size() < 3);
    }
}
