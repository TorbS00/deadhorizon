package gg.deadhorizon.common.scheduling;

public interface TaskScheduler {

    void runLater(Runnable task, long delayTicks);

    void runRepeating(Runnable task, long delayTicks, long periodTicks);

    void cancelAll();

}
