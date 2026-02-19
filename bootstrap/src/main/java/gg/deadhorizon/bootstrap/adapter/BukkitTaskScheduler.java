package gg.deadhorizon.bootstrap.adapter;

import gg.deadhorizon.common.scheduling.TaskScheduler;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitTaskScheduler implements TaskScheduler {

    private final JavaPlugin plugin;

    public BukkitTaskScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runLater(Runnable task, long delayTicks) {
        plugin.getServer().getScheduler().runTaskLater(plugin, task, delayTicks);
    }

    @Override
    public void runRepeating(Runnable task, long delayTicks, long periodTicks) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, task, delayTicks, periodTicks);
    }

    @Override
    public void cancelAll() {
        plugin.getServer().getScheduler().cancelTasks(plugin);
    }
}
