package gg.deadhorizon.bootstrap.config;

import gg.deadhorizon.api.zone.Zone;
import gg.deadhorizon.api.zone.ZoneBounds;
import gg.deadhorizon.api.zone.ZoneLevel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class ZoneConfigLoader {

    private ZoneConfigLoader() {}

    public static List<Zone> load(JavaPlugin plugin) {
        Logger log = plugin.getLogger();

        File file = new File(plugin.getDataFolder(), "zones.yml");
        if (!file.exists()) {
            plugin.saveResource("zones.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<?> zoneList = config.getList("zones");

        if (zoneList == null || zoneList.isEmpty()) {
            log.warning("No zones defined in zones.yml");
            return List.of();
        }

        List<Zone> zones = new ArrayList<>();

        for (int i = 0; i < zoneList.size(); i++) {
            ConfigurationSection section = config.getConfigurationSection("zones." + i);
            if (section == null) continue;

            try {
                String name = section.getString("name");
                int level = section.getInt("level");
                List<Integer> coords = section.getIntegerList("coords");

                if (name == null || name.isBlank()) {
                    log.warning("Zone at index " + i + " has no name, skipping");
                    continue;
                }

                if (coords.size() != 4) {
                    log.warning("Zone '" + name + "' at index " + i
                            + " needs exactly 4 coords [x1, z1, x2, z2], skipping");
                    continue;
                }

                ZoneBounds bounds = new ZoneBounds(
                        coords.get(0), coords.get(1),
                        coords.get(2), coords.get(3)
                );

                Zone zone = new Zone(name, ZoneLevel.fromInt(level), bounds);
                zones.add(zone);

            } catch (IllegalArgumentException e) {
                log.warning("Invalid zone at index " + i + ": " + e.getMessage());
            }
        }

        log.info("Loaded " + zones.size() + " zones from zones.yml");
        return zones;
    }
}