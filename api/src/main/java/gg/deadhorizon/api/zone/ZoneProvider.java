package gg.deadhorizon.api.zone;

import java.util.Collection;
import java.util.Optional;

public interface ZoneProvider {

    ZoneLevel getZoneLevel(int x, int z);

    Optional<Zone> getZone(int x, int z);

    Collection<Zone> getAllZones();

    Collection<Zone> getZonesByName(String townName);
}