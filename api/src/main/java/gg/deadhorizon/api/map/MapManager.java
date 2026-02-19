package gg.deadhorizon.api.map;

public interface MapManager {

    void initialize();
    void scheduleReset();
    long remainingSeconds();
    void shutdown(String reason);
}
