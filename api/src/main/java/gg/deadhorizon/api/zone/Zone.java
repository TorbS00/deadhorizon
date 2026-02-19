package gg.deadhorizon.api.zone;

public record Zone(
        String name,
        ZoneLevel level,
        ZoneBounds bounds
) {

    public Zone {
        if (level == ZoneLevel.WILDERNESS) {
            throw new IllegalArgumentException(
                    "Cannot create a zone with level WILDERNESS. Zones must be level 2-6."
            );
        }
    }

    public boolean contains(int x, int z) {
        return bounds.contains(x, z);
    }
}