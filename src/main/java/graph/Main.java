package graph;

import java.util.List;

import graph.dagsp.DAGShortestPath;
import graph.graph.Graph;
import graph.scc.Kosaraju;
import graph.topo.Kahn;
import io.InputData;
import metrics.Metrics;

public class Main {
    public static void main(String[] args) {
        final String[] DATASETS = new String[]{"small1.json", "small2.json", "small3.json",
                                            "medium1.json", "medium2.json", "medium3.json",
                                            "large1.json", "large2.json", "large3.json"};

        if (args.length == 0) {
            for (String dataset : DATASETS) {
                Graph graph = InputData.inputDataFromJSON(dataset);
                Metrics metrics = new Metrics("smart_city_metrics");
                System.out.println("Dataset: " + dataset);

                Kosaraju scc = new Kosaraju();
                List<List<Integer>> sccs = scc.kosaraju(graph, metrics);
                Graph condensationGraph = scc.buildCondensation(graph, sccs);
                scc.printSCC(sccs);
                metrics.writeToCSV("kosaraju");

                Kahn topoKahn = new Kahn();
                List<Integer> topo = topoKahn.kahn(condensationGraph, metrics);
                topoKahn.printTopo(condensationGraph, sccs, metrics); 
                metrics.writeToCSV("kahn");

                DAGShortestPath dagsp = new DAGShortestPath();
                dagsp.shortestPath(condensationGraph, topo, metrics);
                metrics.writeToCSV("dagShortestPath");
                dagsp.longestPath(condensationGraph, topo, metrics);
                metrics.writeToCSV("dagLongestPath");
            }
        } else {
            Graph graph = InputData.inputDataFromJSON(args[0]);
            Metrics metrics = new Metrics("smart_city_metrics");
            System.out.println("Dataset: " + args[0]);

            Kosaraju scc = new Kosaraju();
            List<List<Integer>> sccs = scc.kosaraju(graph, metrics);
            Graph condensationGraph = scc.buildCondensation(graph, sccs);
            scc.printSCC(sccs);
            metrics.writeToCSV("kosaraju");

            Kahn topoKahn = new Kahn();
            List<Integer> topo = topoKahn.kahn(condensationGraph, metrics);
            topoKahn.printTopo(condensationGraph, sccs, metrics); 
            metrics.writeToCSV("kahn");

            DAGShortestPath dagsp = new DAGShortestPath();
            dagsp.shortestPath(condensationGraph, topo, metrics);
            metrics.writeToCSV("dagShortestPath");
            dagsp.longestPath(condensationGraph, topo, metrics);
            metrics.writeToCSV("dagLongestPath");
        }
    }
}
