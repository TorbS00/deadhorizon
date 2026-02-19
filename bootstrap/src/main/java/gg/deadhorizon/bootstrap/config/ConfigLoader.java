package gg.deadhorizon.bootstrap.config;

import gg.deadhorizon.api.config.DatabaseConfig;
import gg.deadhorizon.api.config.GameConfig;
import gg.deadhorizon.api.config.GeneralConfig;
import gg.deadhorizon.api.map.MapConfig;
import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigLoader {

    private ConfigLoader() {}

    public static GameConfig load(FileConfiguration config) {
        return new GameConfig(
                loadMap(config),
                loadDatabase(config),
                loadGeneral(config)
        );
    }

    private static MapConfig loadMap(FileConfiguration config) {
        return new MapConfig(
                config.getString("map.template-world", "deadhorizon_template"),
                config.getString("map.active-world", "deadhorizon"),
                config.getInt("map.reset-interval-minutes", 240)
        );
    }

    private static DatabaseConfig loadDatabase(FileConfiguration config) {
        return new DatabaseConfig(
                config.getString("database.host", "localhost"),
                config.getInt("database.port", 3306),
                config.getString("database.name", "deadhorizon"),
                config.getString("database.username", "root"),
                config.getString("database.password", ""),
                config.getInt("database.max-pool-size", 10)
        );
    }

    private static GeneralConfig loadGeneral(FileConfiguration config) {
        return new GeneralConfig(
                config.getString("general.server-name", "DeadHorizon"),
                config.getInt("general.max-players", 100),
                config.getBoolean("general.debug", false)
        );
    }
}