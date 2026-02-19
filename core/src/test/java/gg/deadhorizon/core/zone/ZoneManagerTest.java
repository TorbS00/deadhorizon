package gg.deadhorizon.core.zone;

import gg.deadhorizon.api.zone.Zone;
import gg.deadhorizon.api.zone.ZoneBounds;
import gg.deadhorizon.api.zone.ZoneLevel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZoneManagerTest {

    @Test
    void wildernessOutsideAllZones() {
        var zones = List.of(
                new Zone("TestTown", ZoneLevel.ZONE_3, new ZoneBounds(0, 0, 100, 100))
        );
        var manager = new ZoneManager(zones);

        assertEquals(ZoneLevel.WILDERNESS, manager.getZoneLevel(500, 500));
    }

    @Test
    void returnsHighestOverlappingZone() {
        var zones = List.of(
                new Zone("TestTown", ZoneLevel.ZONE_3, new ZoneBounds(0, 0, 100, 100)),
                new Zone("TestTown", ZoneLevel.ZONE_5, new ZoneBounds(30, 30, 70, 70))
        );
        var manager = new ZoneManager(zones);

        assertEquals(ZoneLevel.ZONE_5, manager.getZoneLevel(50, 50));
        assertEquals(ZoneLevel.ZONE_3, manager.getZoneLevel(10, 10));
    }
}