package gg.deadhorizon.api.map;

public record MapConfig(
        String templateWorldName,
        String activeWorldName,
        int resetIntervalMinutes
) {
}
