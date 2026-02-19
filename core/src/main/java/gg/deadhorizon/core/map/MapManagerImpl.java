package gg.deadhorizon.core.map;

import gg.deadhorizon.api.map.MapConfig;
import gg.deadhorizon.api.map.MapManager;
import gg.deadhorizon.api.map.WorldAdapter;
import gg.deadhorizon.api.player.PlayerDataStore;
import gg.deadhorizon.common.scheduling.TaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class MapManagerImpl implements MapManager {

    private static final Logger log = LoggerFactory.getLogger(MapManagerImpl.class);

    private final MapConfig config;
    private final WorldAdapter worldAdapter;
    private final PlayerDataStore playerDataStore;
    private final TaskScheduler scheduler;
    private final AtomicLong resetTimestamp = new AtomicLong(0);

    public MapManagerImpl(MapConfig config,
                          WorldAdapter worldAdapter,
                          PlayerDataStore playerDataStore,
                          TaskScheduler scheduler) {
        this.config = config;
        this.worldAdapter = worldAdapter;
        this.playerDataStore = playerDataStore;
        this.scheduler = scheduler;
    }

    @Override
    public void initialize() {
        if (!worldAdapter.templateExists(config.templateWorldName())) {
            throw new IllegalStateException(
                    "Template world '" + config.templateWorldName() + "' not found. "
                            + "Place your template map in the server folder."
            );
        }

        try {
            worldAdapter.copyTemplate(config.templateWorldName(), config.activeWorldName());
            log.info("Copied template '{}' to active world '{}'",
                    config.templateWorldName(), config.activeWorldName());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to copy template world", e);
        }

        if (!worldAdapter.loadWorld(config.activeWorldName())) {
            throw new IllegalStateException(
                    "Failed to load active world '" + config.activeWorldName() + "'"
            );
        }

        log.info("Active world '{}' loaded successfully", config.activeWorldName());
    }

    @Override
    public void scheduleReset() {
        long resetMillis = config.resetIntervalMinutes() * 60L * 1000L;
        resetTimestamp.set(System.currentTimeMillis() + resetMillis);

        // Warning at 5 minutes
        long warningDelayTicks = (config.resetIntervalMinutes() - 5) * 60L * 20L;
        if (warningDelayTicks > 0) {
            scheduler.runLater(() -> broadcastWarning(5), warningDelayTicks);
        }

        // Warning at 1 minute
        long oneMinWarningTicks = (config.resetIntervalMinutes() - 1) * 60L * 20L;
        if (oneMinWarningTicks > 0) {
            scheduler.runLater(() -> broadcastWarning(1), oneMinWarningTicks);
        }

        // Warning at 10 seconds
        long tenSecWarningTicks = (config.resetIntervalMinutes() * 60L - 10) * 20L;
        if (tenSecWarningTicks > 0) {
            scheduler.runLater(() -> broadcastWarning(0), tenSecWarningTicks);
        }

        // Actual reset
        long resetDelayTicks = config.resetIntervalMinutes() * 60L * 20L;
        scheduler.runLater(this::executeReset, resetDelayTicks);

        log.info("Map reset scheduled in {} minutes", config.resetIntervalMinutes());
    }

    @Override
    public long remainingSeconds() {
        long remaining = resetTimestamp.get() - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }

    @Override
    public void shutdown(String reason) {
        saveAllPlayerLocations();
        worldAdapter.kickAll(reason);
        worldAdapter.stopServer();
    }

    private void executeReset() {
        log.info("Executing map reset...");
        saveAllPlayerLocations();
        worldAdapter.kickAll("§c§lDEAD HORIZON\n\n§7The map is resetting.\n§7Reconnect in a moment.");
        scheduler.runLater(() -> worldAdapter.stopServer(), 20L); // 1 second delay
    }

    private void saveAllPlayerLocations() {
        // This will be called via the bootstrap adapter which provides
        // actual player positions. The WorldAdapter.getOnlinePlayers()
        // gives us UUIDs, but we need positions from the platform layer.
        // For now this is handled by the bootstrap listener on quit/kick.
        log.info("Saving all player data before reset...");
    }

    private void broadcastWarning(int minutesLeft) {
        if (minutesLeft > 0) {
            worldAdapter.broadcast("§c§lDEAD HORIZON §7» §fMap reset in §c" + minutesLeft + " minute(s)§f.");
        } else {
            worldAdapter.broadcast("§c§lDEAD HORIZON §7» §fMap reset in §c10 seconds§f!");
        }
    }
}
