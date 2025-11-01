package metrics;

public interface MetricsInterface {
    void addVisits();
    void addEdges();
    void addPushes();
    void addPops();
    void addRelaxations();
    void startTimer(String name);
    void stopTimer(String name);

    int getVisits();
    int getEdges();
    int getPushes();
    int getPops();
    int getRelaxations();
    double getTimeMs(String name);

    void reset();
}
