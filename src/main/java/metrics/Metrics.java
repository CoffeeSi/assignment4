package metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Metrics implements MetricsInterface {
    private String filename;
    private int visits = 0;
    private int edges = 0;
    private int pushes = 0;
    private int pops = 0;
    private int relaxations = 0;

    private final Map<String, Long> namedTimers = new HashMap<>();
    private final Map<String, Deque<Long>> startStacks = new HashMap<>();

    public Metrics(String filename) {
        this.filename = filename + ".csv";

        File file = new File(this.filename);
        boolean writeHeader = (!file.exists() || file.length() == 0);

        try (FileWriter writer = new FileWriter(this.filename, true)) {
            if (writeHeader)
                writer.write("algorithm,visits,edges,pushes,pops,relaxations,timeMs\n");
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize CSV file", e);
        }
    }

    @Override
    public void addVisits() { visits++; }

    @Override
    public void addEdges() { edges++; }

    @Override
    public void addPushes() { pushes++; }

    @Override
    public void addPops() { pops++; }

    @Override
    public void addRelaxations() { relaxations++; }

    @Override
    public void startTimer(String name) {
        long now = System.nanoTime();
        startStacks.computeIfAbsent(name, k -> new ArrayDeque<>()).push(now);
    }

    @Override
    public void stopTimer(String name) {
        Deque<Long> stack = startStacks.get(name);
        if (stack == null || stack.isEmpty()) return;
        long start = stack.pop();
        long elapsed = System.nanoTime() - start;
        namedTimers.put(name, namedTimers.getOrDefault(name, 0L) + elapsed);
    }

    // Getters
    @Override
    public int getVisits() { return visits; }

    @Override
    public int getEdges() { return edges; }

    @Override
    public int getPushes() { return pushes; }

    @Override
    public int getPops() { return pops; }

    @Override
    public int getRelaxations() { return relaxations; }

    @Override
    public double getTimeMs(String name) { 
        long time = namedTimers.getOrDefault(name, 0L);
        return time/1_000_000.0; 
    }

    @Override
    public void reset() {
        visits = edges = pushes = pops = relaxations = 0;
        namedTimers.clear();
        startStacks.clear();
    }

    public void writeToCSV(String algorithm) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(String.format("%s,%d,%d,%d,%d,%d,%.4f%n", 
                algorithm, getVisits(), getEdges(), getPushes(), 
                getPops(), getRelaxations(), getTimeMs(algorithm)));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
