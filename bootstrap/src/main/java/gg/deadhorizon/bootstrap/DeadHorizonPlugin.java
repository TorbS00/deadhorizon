package gg.deadhorizon.bootstrap;

import gg.deadhorizon.api.map.MapConfig;
import gg.deadhorizon.bootstrap.adapter.BukkitTaskScheduler;
import gg.deadhorizon.bootstrap.adapter.BukkitWorldAdapter;
import gg.deadhorizon.bootstrap.config.ConfigLoader;
import gg.deadhorizon.bootstrap.config.ZoneConfigLoader;
import gg.deadhorizon.core.map.MapManagerImpl;
import gg.deadhorizon.core.zone.ZoneManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeadHorizonPlugin extends JavaPlugin {

    private MapManagerImpl mapManager;
    private ZoneManager zoneManager;

    @Override
    public void onEnable() {
        getLogger().info("DeadHorizon is starting...");

        // Config
        saveDefaultConfig();
        var gameConfig = ConfigLoader.load(getConfig());

        // Infrastructure
        var scheduler = new BukkitTaskScheduler(this);
        var worldAdapter = new BukkitWorldAdapter(this);

        // Zones
        var zones = ZoneConfigLoader.load(this);
        zoneManager = new ZoneManager(zones);

        // Map manager
        mapManager = new MapManagerImpl(
                gameConfig.map(), worldAdapter, null, scheduler
        );
        /*
        try {
            mapManager.initialize();
            mapManager.scheduleReset();
        } catch (IllegalStateException e) {
            getLogger().severe("Failed to initialize map: " + e.getMessage());
            getServer().shutdown();
            return;
        }
         */

        getLogger().info("DeadHorizon loaded successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DeadHorizon shutting down...");
    }
}
