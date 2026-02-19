package gg.deadhorizon.core.zone;

import gg.deadhorizon.api.zone.Zone;
import gg.deadhorizon.api.zone.ZoneLevel;
import gg.deadhorizon.api.zone.ZoneProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ZoneManager implements ZoneProvider {

    private static final Logger log = LoggerFactory.getLogger(ZoneManager.class);

    private final List<Zone> zones;

    public ZoneManager(List<Zone> zones) {
        this.zones = List.copyOf(zones); // immutable copy
        log.info("Loaded {} zones across {} towns",
                zones.size(),
                zones.stream().map(Zone::name).distinct().count()
        );
    }

    @Override
    public ZoneLevel getZoneLevel(int x, int z) {
        return zones.stream()
                .filter(zone -> zone.contains(x, z))
                .map(Zone::level)
                .max(Comparator.comparingInt(ZoneLevel::level))
                .orElse(ZoneLevel.WILDERNESS);
    }

    @Override
    public Optional<Zone> getZone(int x, int z) {
        return zones.stream()
                .filter(zone -> zone.contains(x, z))
                .max(Comparator.comparingInt(zone -> zone.level().level()));
    }

    @Override
    public Collection<Zone> getAllZones() {
        return zones;
    }

    @Override
    public Collection<Zone> getZonesByName(String townName) {
        return zones.stream()
                .filter(zone -> zone.name().equalsIgnoreCase(townName))
                .collect(Collectors.toUnmodifiableList());
    }

    public int townCount() {
        return (int) zones.stream().map(Zone::name).distinct().count();
    }
}